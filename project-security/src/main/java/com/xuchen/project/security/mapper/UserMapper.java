package com.xuchen.project.security.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.project.model.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> selectAuthoritiesByUserId(Long userId);
}
