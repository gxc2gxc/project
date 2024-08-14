package com.xuchen.project.security.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.project.model.security.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
