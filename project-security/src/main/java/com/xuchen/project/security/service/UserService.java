package com.xuchen.project.security.service;

import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.UserDto;
import com.xuchen.project.model.security.dto.UserRoleDto;
import com.xuchen.project.model.security.pojo.User;
import com.xuchen.project.model.security.vo.UserLoginVo;
import com.xuchen.project.model.security.vo.UserVerifyVo;

import java.util.List;

public interface UserService {

    /**
     * 新增用户
     *
     * @param userDto 新增用户Dto
     * @return 执行结果
     */
    ResponseResult<Long> insert(UserDto userDto);

    /**
     * 删除用户
     *
     * @param userIds 删除用户Id列表
     * @return 执行结果
     */
    ResponseResult<Object> delete(List<Long> userIds);

    /**
     * 修改用户
     *
     * @param userDto 修改用户Dto
     * @return 执行结果
     */
    ResponseResult<Object> update(UserDto userDto);

    /**
     * 查询用户
     *
     * @param userDto 查询用户Dto
     * @return 执行结果
     */
    ResponseResult<List<User>> select(UserDto userDto);

    /**
     * 为用户绑定角色
     *
     * @param userRoleDto 用户与角色的Id
     * @return 执行结果
     */
    ResponseResult<Object> bindingRole(UserRoleDto userRoleDto);

    /**
     * 用户登录
     *
     * @param userDto 用户登录Dto
     * @return 执行结果
     */
    ResponseResult<UserLoginVo> login(UserDto userDto);

    /**
     * 校验Jwt令牌
     *
     * @param token Jwt令牌
     * @return 执行结果
     */
    ResponseResult<UserVerifyVo> verify(String token);

    /**
     * 用户退出
     *
     * @return 执行结果
     */
    ResponseResult<Object> logout();
}
