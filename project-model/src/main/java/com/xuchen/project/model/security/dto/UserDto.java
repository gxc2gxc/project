package com.xuchen.project.model.security.dto;

import com.xuchen.project.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull(message = "用户Id不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Delete.class})
    private Long userId;

    @NotBlank(message = "用户名不能为空", groups = {ValidationGroup.Insert.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {ValidationGroup.Insert.class})
    private String password;

    private Integer status;
}
