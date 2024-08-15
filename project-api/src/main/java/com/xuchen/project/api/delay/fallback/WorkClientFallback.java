package com.xuchen.project.api.delay.fallback;

import com.xuchen.project.api.delay.WorkClient;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.delay.dto.WorkDto;
import org.springframework.cloud.openfeign.FallbackFactory;

public class WorkClientFallback implements FallbackFactory<WorkClient> {

    @Override
    public WorkClient create(Throwable cause) {

        return new WorkClient() {
            @Override
            public ResponseResult<Long> insert(WorkDto workDto) {
                return ResponseResult.error(ResponseStatus.CIRCUIT_BREAKER_EXCEPTION);
            }

            @Override
            public ResponseResult<Object> delete(WorkDto workDto) {
                return ResponseResult.error(ResponseStatus.CIRCUIT_BREAKER_EXCEPTION);
            }

            @Override
            public ResponseResult<Object> update(WorkDto workDto) {
                return ResponseResult.error(ResponseStatus.CIRCUIT_BREAKER_EXCEPTION);
            }
        };
    }
}
