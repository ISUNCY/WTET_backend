package org.isuncy.wtet_backend.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {
    private String username;    //用户名
    private String nickname;    //昵称
    private String auth;        //令牌
}
