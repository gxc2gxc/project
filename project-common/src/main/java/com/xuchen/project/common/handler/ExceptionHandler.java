package com.xuchen.project.common.handler;

import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.exception.ServerException;
import com.xuchen.project.model.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
public class ExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ClientException.class)
    public ResponseResult<Object> clientRuntimeException(ClientException e) {
        log.error("客户端异常：{}", e.getMessage(), e);
        return ResponseResult.error(e.getStatus());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ServerException.class)
    public ResponseResult<Object> serverRuntimeException(ServerException e) {
        log.error("服务端异常：{}", e.getMessage(), e);
        return ResponseResult.error(e.getStatus());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseResult<Object> exception(RuntimeException e) {
        log.error("未知异常：{}", e.getMessage(), e);
        return ResponseResult.error();
    }
}
