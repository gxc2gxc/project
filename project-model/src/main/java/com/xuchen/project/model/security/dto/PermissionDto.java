package com.xuchen.project.model.security.dto;

import com.xuchen.project.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PermissionDto {

    @NotNull(message = "权限Id不能为空", groups = {ValidationGroup.Update.class})
    private Long permissionId;

    @NotBlank(message = "权限名不能为空", groups = {ValidationGroup.Insert.class})
    private String permissionName;

    private Integer status;
}
