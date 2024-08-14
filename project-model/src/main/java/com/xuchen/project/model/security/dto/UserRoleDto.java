package com.xuchen.project.model.security.dto;

import com.xuchen.project.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRoleDto {

    @NotNull(message = "用户Id不能为空", groups = {ValidationGroup.Insert.class, ValidationGroup.Delete.class})
    private Long userId;

    @NotNull(message = "角色ID不能为空", groups = {ValidationGroup.Insert.class, ValidationGroup.Delete.class})
    private Long roleId;
}
