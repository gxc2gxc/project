package com.xuchen.project.gateway.filter;

import com.xuchen.project.api.SecurityClient;
import com.xuchen.project.gateway.config.RequiredAuthProperties;
import com.xuchen.project.model.common.constant.HeaderConstant;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
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
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 认证鉴权结束，放行请求
        log.info("用户拥有访问权限");
        ServerWebExchange webExchange = exchange.mutate()
                .request(builder -> builder.header(
                        HeaderConstant.INNER_HEADER_NAME,
                        String.valueOf(userId)
                )).build();
        return chain.filter(webExchange);
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
