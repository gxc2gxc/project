package com.xuchen.project.api.security.fallback;

import com.xuchen.project.api.security.SecurityClient;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import org.springframework.cloud.openfeign.FallbackFactory;

public class SecurityClientFallback implements FallbackFactory<SecurityClient> {

    @Override
    public SecurityClient create(Throwable cause) {
        return new SecurityClient() {
            @Override
            public ResponseResult<UserVerifyVo> verify(String token) {
                return ResponseResult.error(ResponseStatus.CIRCUIT_BREAKER_EXCEPTION);
            }
        };
    }
}
