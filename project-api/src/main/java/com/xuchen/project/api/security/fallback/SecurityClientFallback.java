package com.xuchen.project.api.security.fallback;

import com.xuchen.project.api.security.SecurityClient;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.vo.ResponseResult;
import org.springframework.cloud.openfeign.FallbackFactory;

public class SecurityClientFallback implements FallbackFactory<SecurityClient> {

    @Override
    public SecurityClient create(Throwable cause) {
        return token -> ResponseResult.error(ResponseStatus.CIRCUIT_BREAKER_EXCEPTION);
    }
}
