package com.xuchen.project.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.xuchen.project.api.security.SecurityClient;
import com.xuchen.project.gateway.config.RequiredAuthProperties;
import com.xuchen.project.model.common.constant.HeaderConstant;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.exception.ServerException;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.gateway.constant.GatewayConstant;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    private final SecurityClient securityClient;

    private final RequiredAuthProperties requiredAuthProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final HashMap<String, Set<String>> requiredAuthMap = new HashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断请求是否需要权限
        log.info("网关收到请求：{}", exchange.getRequest().getURI());
        ServerHttpRequest request = exchange.getRequest();

        try {
            // 不需要权限时放行
            Set<String> requiredAuths = requiredAuths(request.getPath().toString());
            log.info("接口所需权限：{}", requiredAuths);
            if (requiredAuths.isEmpty()) {
                return chain.filter(exchange);
            }

            // 获取请求头中的Token
            String token = null;
            List<String> headers = request.getHeaders().get(HeaderConstant.OUTER_HEADER_NAME);
            if (headers != null && !headers.isEmpty()) {
                token = headers.get(0);
            }

            // 获取用户拥有的权限
            boolean verify = false;
            ResponseResult<UserVerifyVo> responseResult = null;
            if (token != null) {
                responseResult = securityClient.verify(token);
                if (responseResult != null && responseResult.getCode() == ResponseStatus.SUCCESS.getCode()) {
                    verify = true;
                }
            }
            if (!verify) {
                log.info("用户没有经过认证");
                throw new ClientException(ResponseStatus.USER_NOLOGGING_EXCEPTION);
            }

            // 检查用户是否拥有所需权限
            boolean hasAuth = false;
            Long userId = responseResult.getData().getUserId();
            List<String> authorities = responseResult.getData().getAuthorities();
            log.info("用户拥有权限：{}", authorities);
            for (String authority : authorities) {
                if (requiredAuths.contains(authority)) {
                    hasAuth = true;
                    break;
                }
            }
            if (!hasAuth) {
                log.info("用户没有访问权限");
                throw new ClientException(ResponseStatus.PERMISSION_DENY_EXCEPTION);
            }

            // 认证鉴权结束，放行请求
            log.info("用户拥有访问权限");
            exchange = exchange.mutate().request(builder -> builder.header(HeaderConstant.INNER_HEADER_NAME, String.valueOf(userId))).build();
        } catch (ClientException e) {
            log.info("客户端异常：{}", e.getMessage());
            exchange = redirectRequest(exchange, ClientException.class.getName(), e.getStatus());
        } catch (ServerException e) {
            log.info("服务端异常：{}", e.getMessage());
            exchange = redirectRequest(exchange, ServerException.class.getName(), e.getStatus());
        } catch (Exception e) {
            log.info("未知异常：{}", e.getMessage());
            exchange = redirectRequest(exchange, Exception.class.getName(), ResponseStatus.DEFAULT_SERVER_EXCEPTION);
        }
        return chain.filter(exchange);
    }

    /**
     * 将异常重定向到网关的Controller中
     *
     * @param exchange 请求
     * @return 重定向的请求
     */
    private ServerWebExchange redirectRequest(ServerWebExchange exchange, String exceptionType, ResponseStatus status) {
        ServerHttpRequest request = exchange.getRequest();
        return exchange.mutate().request(
                request.mutate()
                        .method(HttpMethod.GET)
                        .header(GatewayConstant.EXCEPTION_TYPE_NAME, exceptionType)
                        .header(GatewayConstant.EXCEPTION_STATUS_NAME, JSON.toJSONString(status))
                        .path(GatewayConstant.EXCEPTION_CONTROLLER_PATH).build()
        ).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 加载接口访问权限
     *
     * @param uri 接口的请求路径
     * @return 所需权限集合
     */
    private Set<String> requiredAuths(String uri) {
        // 加载接口访问权限配置
        if (requiredAuthMap.isEmpty()) {
            for (String requiredAuthUrl : requiredAuthProperties.getRequiredAuthUrls()) {
                String url = requiredAuthUrl.split("=")[0];
                Set<String> authorities = new HashSet<>();
                if (!requiredAuthUrl.endsWith("=")) {
                    authorities = new HashSet<>(Arrays.asList(requiredAuthUrl.split("=")[1].split(",")));
                }
                requiredAuthMap.put(url, authorities);
            }
        }

        // 获取当前接口需要的权限
        for (String requiredAuthUrl : requiredAuthMap.keySet()) {
            if (antPathMatcher.match(requiredAuthUrl, uri)) {
                return requiredAuthMap.get(requiredAuthUrl);
            }
        }
        return new HashSet<>();
    }
}
