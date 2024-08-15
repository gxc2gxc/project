package com.xuchen.project.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.exception.ServerException;
import com.xuchen.project.model.gateway.constant.GatewayConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    /**
     * 将过滤器中的异常转发至Controller层
     *
     * @throws Exception 异常类型
     */
    @GetMapping(GatewayConstant.EXCEPTION_CONTROLLER_PATH)
    public void handlerException(
            @RequestParam(GatewayConstant.EXCEPTION_TYPE_NAME) String exceptionType,
            @RequestParam(GatewayConstant.EXCEPTION_STATUS_NAME) String status) throws Exception {
        ResponseStatus responseStatus = JSON.parseObject(status, ResponseStatus.class);

        Exception exception;
        if (exceptionType.equals(ClientException.class.getName())) {
            exception = new ClientException(responseStatus);
        } else if (exceptionType.equals(ServerException.class.getName())) {
            exception = new ServerException(responseStatus);
        } else {
            exception = new ServerException(responseStatus);
        }
        throw exception;
    }
}
