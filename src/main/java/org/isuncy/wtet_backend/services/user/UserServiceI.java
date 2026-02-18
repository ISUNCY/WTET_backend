package org.isuncy.wtet_backend.services.user;

import org.isuncy.wtet_backend.entities.dto.UserLoginDTO;
import org.isuncy.wtet_backend.entities.dto.UserRegisterDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.UserLoginVO;
import org.springframework.stereotype.Service;

public interface UserServiceI {
    //创建新用户
    Result<String> createUser(UserRegisterDTO userRegisterDTO);

    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);
}
