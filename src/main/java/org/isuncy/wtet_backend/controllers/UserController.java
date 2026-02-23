package org.isuncy.wtet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.dto.UserLoginDTO;
import org.isuncy.wtet_backend.entities.dto.UserRegisterDTO;
import org.isuncy.wtet_backend.entities.dto.UserUpdateDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.UserInfoVO;
import org.isuncy.wtet_backend.entities.vo.UserLoginVO;
import org.isuncy.wtet_backend.services.user.UserServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceI userService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/register")
    @Operation(summary = "注册用户")
    @OperLog(module = "用户", logType = LogType.INFO, comment = "用户注册")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (StringUtils.hasLength(userRegisterDTO.getUsername()) && StringUtils.hasLength(userRegisterDTO.getPassword())) {
            return userService.createUser(userRegisterDTO);
        }
        return new Result<String>().error("字段不全");
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @OperLog(module = "用户", logType = LogType.INFO, comment = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        if (!StringUtils.hasLength(userLoginDTO.getUsername())
        || !StringUtils.hasLength(userLoginDTO.getPassword())) {
            return new Result<UserLoginVO>().error("字段不全");
        }
        return userService.login(userLoginDTO);
    }

    @PostMapping("/update")
    @Operation(summary = "更新用户信息")
    @OperLog(module = "用户", logType = LogType.INFO, comment = "更新用户信息")
    public Result<String> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        if (!StringUtils.hasLength(userUpdateDTO.getUsername())
        || !StringUtils.hasLength(userUpdateDTO.getPassword())
        || !StringUtils.hasLength(userUpdateDTO.getNickname())) {
            return new Result<String>().error("error");
        }
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("未登录！");
        }
        return userService.updateInfo(userId, userUpdateDTO);
    }

    @GetMapping("/getInfo")
    @Operation(summary = "获取用户信息")
    @OperLog(module = "用户", logType = LogType.INFO, comment = "获取用户详细信息")
    public Result<UserInfoVO> getSelfInfo() {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<UserInfoVO>().unAuth("未登录");
        }
        return userService.getUserInfo(userId);
    }

}
