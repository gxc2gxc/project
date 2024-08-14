package com.xuchen.project.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuchen.project.common.util.UserContext;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.constant.SecurityConstant;
import com.xuchen.project.model.security.dto.UserDto;
import com.xuchen.project.model.security.dto.UserRoleDto;
import com.xuchen.project.model.security.pojo.Role;
import com.xuchen.project.model.security.pojo.User;
import com.xuchen.project.model.security.pojo.UserRole;
import com.xuchen.project.model.security.vo.UserLoginVo;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import com.xuchen.project.security.config.JwtProperties;
import com.xuchen.project.security.mapper.RoleMapper;
import com.xuchen.project.security.mapper.UserMapper;
import com.xuchen.project.security.mapper.UserRoleMapper;
import com.xuchen.project.security.service.UserService;
import com.xuchen.project.security.util.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 新增用户
     *
     * @param userDto 新增用户Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Long> insert(UserDto userDto) {
        log.info("新增用户：{}", userDto);

        // 检查用户名是否已存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDto.getUsername()));
        if (user != null) {
            throw new ClientException(ResponseStatus.USERNAME_EXISTED_EXCEPTION);
        }

        // 新增用户
        user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);

        // 返回执行结果
        return ResponseResult.success(user.getUserId());
    }

    /**
     * 删除用户
     *
     * @param userIds 删除用户Id列表
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> delete(List<Long> userIds) {
        log.info("删除用户：{}", userIds);

        // 删除用户
        userMapper.delete(Wrappers.<User>lambdaQuery().in(User::getUserId, userIds));

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 修改用户
     *
     * @param userDto 修改用户Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> update(UserDto userDto) {
        log.info("修改用户：{}", userDto);

        // 检查用户是否已存在
        User user = userMapper.selectById(userDto.getUserId());
        if (user == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXIST_EXCEPTION);
        }

        // 修改用户
        BeanUtils.copyProperties(userDto, user);
        userMapper.updateById(user);

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 查询用户
     *
     * @param userDto 查询用户Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<List<User>> select(UserDto userDto) {
        log.info("查询用户：{}", userDto);

        // 查询用户
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().like(User::getUsername, userDto.getUsername()).orderByDesc(User::getCreateTime));

        // 返回执行结果
        return ResponseResult.success(users);
    }

    /**
     * 为用户绑定角色
     *
     * @param userRoleDto 用户与角色的Id
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> bindingRole(UserRoleDto userRoleDto) {
        log.info("为用户绑定角色：{}", userRoleDto);

        // 检查用户是否存在
        User user = userMapper.selectById(userRoleDto.getUserId());
        if (user == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXIST_EXCEPTION);
        }

        // 检查角色是否存在
        Role role = roleMapper.selectById(userRoleDto.getRoleId());
        if (role == null) {
            throw new ClientException(ResponseStatus.ROLE_NOT_EXIST_EXCEPTION);
        }

        // 检查绑定关系是否存在
        UserRole userRole = userRoleMapper.selectOne(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userRoleDto.getUserId()).eq(UserRole::getRoleId, userRoleDto.getRoleId()));
        if (userRole != null) {
            throw new ClientException(ResponseStatus.USER_ROLE_BINDING_EXIST_EXCEPTION);
        }

        // 绑定角色
        userRole = new UserRole();
        BeanUtils.copyProperties(userRoleDto, userRole);
        userRoleMapper.insert(userRole);
        return ResponseResult.success();
    }

    /**
     * 用户登录
     *
     * @param userDto 用户登录Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<UserLoginVo> login(UserDto userDto) {
        log.info("用户登录：{}", userDto);

        // 检查用户名是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDto.getUsername()));
        if (user == null) {
            throw new ClientException(ResponseStatus.USERNAME_EXISTED_EXCEPTION);
        }

        // 检查密码是否一致
        boolean matches = passwordEncoder.matches(userDto.getPassword(), user.getPassword());
        if (!matches) {
            throw new ClientException(ResponseStatus.WRONG_PASSWORD_EXCEPTION);
        }

        // 用户名和密码一致，生成Jwt令牌并保存到Redis
        String token = jwtTool.createToken(user.getUserId());
        user.setPassword(token);    // 使用密码临时保存Token
        redisTemplate.opsForValue().set(SecurityConstant.REDIS_TOKEN_PREFIX + user.getUserId(), JSON.toJSONString(user), jwtProperties.getTtl());

        // 返回执行结果
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(user.getUserId());
        userLoginVo.setToken(token);
        return ResponseResult.success(userLoginVo);
    }

    /**
     * 校验Jwt令牌
     *
     * @param token Jwt令牌
     * @return 执行结果
     */
    @Override
    public ResponseResult<UserVerifyVo> verify(String token) {
        log.info("校验Jwt令牌：{}", token);

        // 验证Jwt令牌
        Long userId = jwtTool.parseToken(token);
        if (userId == null) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        // 验证Redis中的白名单
        String jsonUser = redisTemplate.opsForValue().get(SecurityConstant.REDIS_TOKEN_PREFIX + userId);
        User user = JSON.parseObject(jsonUser, User.class);
        if (user == null || !token.equals(user.getPassword())) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        // 返回用户权限
        UserVerifyVo userVerifyVo = new UserVerifyVo();
        userVerifyVo.setUserId(userId);
        userVerifyVo.setAuthorities(userMapper.selectAuthoritiesByUserId(userId));
        return ResponseResult.success(userVerifyVo);
    }

    /**
     * 用户退出
     *
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> logout() {
        log.info("用户退出：{}", UserContext.getUser());

        // 从Redis白名单移除用户并返回
        redisTemplate.delete(SecurityConstant.REDIS_TOKEN_PREFIX + UserContext.getUser());
        return ResponseResult.success();
    }
}
