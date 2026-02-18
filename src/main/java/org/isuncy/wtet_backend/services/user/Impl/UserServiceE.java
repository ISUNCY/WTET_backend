package org.isuncy.wtet_backend.services.user.Impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import org.isuncy.wtet_backend.entities.dto.UserLoginDTO;
import org.isuncy.wtet_backend.entities.dto.UserRegisterDTO;
import org.isuncy.wtet_backend.entities.pojo.User;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.UserLoginVO;
import org.isuncy.wtet_backend.mapper.user.UserMapper;
import org.isuncy.wtet_backend.services.user.UserServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceE implements UserServiceI {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result<String> createUser(UserRegisterDTO userRegisterDTO) {
        //检查用户名是否冲突
        boolean exist = userMapper.exists(new QueryWrapper<User>().eq("username", userRegisterDTO.getUsername()));
        if (exist) {
            return new Result<String>().error("Username already exists");
        }
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(DigestUtil.sha256Hex(userRegisterDTO.getPassword()));
        user.setNickname(userRegisterDTO.getNickname());
        user.setId(UUID.randomUUID().toString());
        userMapper.insert(user);
        return new Result<String>().success();
    }

    @Override
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userLoginDTO.getUsername()).eq("password", DigestUtil.sha256Hex(userLoginDTO.getPassword())));
        if (user == null) {
            return new Result<UserLoginVO>().error("login failed");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", userLoginDTO.getUsername());
        claims.put("nickname", user.getNickname());
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setAuth(JwtHelper.createJWT(claims));
        userLoginVO.setUsername(userLoginDTO.getUsername());
        userLoginVO.setNickname(user.getNickname());
        return new Result<UserLoginVO>().success(userLoginVO);
    }

}
