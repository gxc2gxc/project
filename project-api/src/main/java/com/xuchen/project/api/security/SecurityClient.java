package com.xuchen.project.api.security;

import com.xuchen.project.api.security.fallback.SecurityClientFallback;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "project-security", fallback = SecurityClientFallback.class)
public interface SecurityClient {

    /**
     * 校验Jwt令牌
     *
     * @param token Jwt令牌
     * @return 执行结果
     */
    @GetMapping("/security/user/verify")
    ResponseResult<UserVerifyVo> verify(@RequestParam("token") String token);
}
