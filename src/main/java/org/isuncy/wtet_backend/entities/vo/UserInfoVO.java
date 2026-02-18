package org.isuncy.wtet_backend.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    private String username;
    private String nickname;
    private Double weight;
    private Double height;
}
