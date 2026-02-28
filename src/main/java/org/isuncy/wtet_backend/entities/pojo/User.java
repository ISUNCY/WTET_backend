package org.isuncy.wtet_backend.entities.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bs_user")
public class User {
    @TableId(type = IdType.AUTO)
    private String id;
    private String username;
    private String nickname;
    private String password;
    private Double weight;
    private Double height;
    private String preference;  //饮食偏好
    private boolean dataImport;
}
