package com.xuchen.project.model.security.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVerifyVo {

    private Long userId;

    private List<String> authorities;
}
