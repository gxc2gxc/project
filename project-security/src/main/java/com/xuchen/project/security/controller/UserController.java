package com.xuchen.project.security.controller;

import com.xuchen.project.api.SecurityClient;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.UserDto;
import com.xuchen.project.model.security.dto.UserRoleDto;
import com.xuchen.project.model.security.pojo.User;
import com.xuchen.project.model.security.vo.UserLoginVo;
import com.xuchen.project.model.security.vo.UserVerifyVo;
import com.xuchen.project.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/user")
public class UserController implements SecurityClient {

    private final UserService userService;

    /**
     * 新增用户
     *
     * @param userDto 新增用户Dto
     * @return 执行结果
     */
    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody UserDto userDto) {
        return userService.insert(userDto);
    }

    /**
     * 删除用户
     *
     * @param userIds 删除用户Id列表
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("userIds") List<Long> userIds) {
        return userService.delete(userIds);
    }

    /**
     * 修改用户
     *
     * @param userDto 修改用户Dto
     * @return 执行结果
     */
    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    /**
     * 查询用户
     *
     * @param userDto 查询用户Dto
     * @return 执行结果
     */
    @GetMapping("/select")
    public ResponseResult<List<User>> select(@RequestBody UserDto userDto) {
        return userService.select(userDto);
    }

    /**
     * 为用户绑定角色
     *
     * @param userRoleDto 用户与角色的Id
     * @return 执行结果
     */
    @PostMapping("/bindingRole")
    public ResponseResult<Object> bindingRole(@RequestBody UserRoleDto userRoleDto) {
        return userService.bindingRole(userRoleDto);
    }

    /**
     * 用户登录
     *
     * @param userDto 用户登录Dto
     * @return 执行结果
     */
    @PostMapping("/login")
    public ResponseResult<UserLoginVo> login(@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }

    /**
     * 校验Jwt令牌
     *
     * @param token Jwt令牌
     * @return 执行结果
     */
    @GetMapping("/verify")
    public ResponseResult<UserVerifyVo> verify(@RequestParam("token") String token) {
        return userService.verify(token);
    }

    /**
     * 用户退出
     *
     * @return 执行结果
     */
    @GetMapping("/logout")
    public ResponseResult<Object> logout() {
        return userService.logout();
    }
}
