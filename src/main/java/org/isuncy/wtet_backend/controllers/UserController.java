package org.isuncy.wtet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.dto.UserLoginDTO;
import org.isuncy.wtet_backend.entities.dto.UserRegisterDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.UserLoginVO;
import org.isuncy.wtet_backend.services.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceI userService;

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
}
