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
@TableName("tb_security_user_role")
public class UserRole {

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "role_id")
    private Long roleId;
}
