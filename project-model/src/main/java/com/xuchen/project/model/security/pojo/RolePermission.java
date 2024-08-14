package com.xuchen.project.model.security.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_security_role_permission")
public class RolePermission {

    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "permission_id")
    private Long permissionId;
}
