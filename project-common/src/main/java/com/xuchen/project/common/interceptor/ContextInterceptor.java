package com.xuchen.project.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xuchen.project.common.util.UserContext;
import com.xuchen.project.model.common.constant.HeaderConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的用户信息并保存到UserContext
        String userId = request.getHeader(HeaderConstant.INNER_HEADER_NAME);
        if (StringUtils.isNotBlank(userId)) {
            log.info("获取请求头中的用户信息并保存到UserContext：{}", userId);
            UserContext.setUser(Long.valueOf(userId));
        }

        // 认证授权由网关和认证服务进行，此处直接放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 避免ThreadLocal内存泄漏问题
        UserContext.removeUser();
    }
}
