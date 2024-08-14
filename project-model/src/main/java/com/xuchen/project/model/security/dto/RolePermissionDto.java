package com.xuchen.project.model.security.dto;

import com.xuchen.project.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RolePermissionDto {

    @NotNull(message = "角色Id不能为空", groups = {ValidationGroup.Insert.class, ValidationGroup.Delete.class})
    private Long roleId;

    @NotNull(message = "权限Id不能为空", groups = {ValidationGroup.Insert.class, ValidationGroup.Delete.class})
    private Long permissionId;
}
