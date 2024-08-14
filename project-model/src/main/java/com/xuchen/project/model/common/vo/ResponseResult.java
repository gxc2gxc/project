package com.xuchen.project.model.common.vo;

import com.xuchen.project.model.common.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

    private int code;

    private String message;

    private T data;

    // 执行成功
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data);
    }

    // 执行失败
    public static <T> ResponseResult<T> error() {
        return new ResponseResult<>(ResponseStatus.DEFAULT_SERVER_EXCEPTION.getCode(), ResponseStatus.DEFAULT_SERVER_EXCEPTION.getMessage(), null);
    }

    public static <T> ResponseResult<T> error(ResponseStatus status) {
        return new ResponseResult<>(status.getCode(), status.getMessage(), null);
    }
}
