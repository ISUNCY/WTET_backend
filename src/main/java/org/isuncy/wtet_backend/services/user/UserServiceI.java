package org.isuncy.wtet_backend.services.user;

import org.isuncy.wtet_backend.entities.dto.UserLoginDTO;
import org.isuncy.wtet_backend.entities.dto.UserPassUpdateDTO;
import org.isuncy.wtet_backend.entities.dto.UserRegisterDTO;
import org.isuncy.wtet_backend.entities.dto.UserUpdateDTO;
import org.isuncy.wtet_backend.entities.pojo.User;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.UserInfoVO;
import org.isuncy.wtet_backend.entities.vo.UserLoginVO;
import org.springframework.stereotype.Service;

public interface UserServiceI {
    //创建新用户
    Result<String> createUser(UserRegisterDTO userRegisterDTO);

    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);

    Result<String> updateInfo(String userId, UserUpdateDTO userUpdateDTO);

    Result<UserInfoVO> getUserInfo(String userId);

    Result<String> updatePass(String userId, UserPassUpdateDTO userPassUpdateDTO);
}
