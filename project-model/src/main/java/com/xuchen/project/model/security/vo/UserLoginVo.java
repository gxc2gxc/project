package com.xuchen.project.model.security.vo;

import lombok.Data;

@Data
public class UserLoginVo {

    private Long userId;

    private String token;
}
