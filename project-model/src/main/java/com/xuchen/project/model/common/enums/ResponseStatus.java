package com.xuchen.project.model.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    // 200：执行成功
    SUCCESS(200, "执行成功"),

    // 6xx: 由于客户端原因导致执行失败
    DEFAULT_CLIENT_EXCEPTION(600, "默认客户端错误"),
    USER_NOLOGGING_EXCEPTION(601, "用户尚未登录"),
    INVALID_TOKEN_EXCEPTION(602, "无效的Jwt令牌"),
    EXPIRED_TOKEN_EXCEPTION(603, "过期的Jwt令牌"),
    USERNAME_EXISTED_EXCEPTION(604, "用户名已存在"),
    USER_NOT_EXIST_EXCEPTION(605, "用户不存在"),
    WRONG_PASSWORD_EXCEPTION(606, "密码错误"),
    USER_ROLE_BINDING_EXIST_EXCEPTION(607, "用户角色已绑定"),
    ROLE_NAME_EXISTED_EXCEPTION(608, "角色名已存在"),
    PERMISSION_NAME_EXISTED_EXCEPTION(609, "权限名已存在"),
    ROLE_NOT_EXIST_EXCEPTION(610, "角色不存在"),
    PERMISSION_NOT_EXIST_EXCEPTION(611, "权限不存在"),


    // 7xx: 由于服务端原因导致执行失败
    DEFAULT_SERVER_EXCEPTION(700, "默认服务端错误");

    private final int code;

    private final String message;
}
