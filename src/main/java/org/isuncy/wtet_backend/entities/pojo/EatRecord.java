package org.isuncy.wtet_backend.entities.pojo;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bs_eat_record")
public class EatRecord {
    private String id;
    private String dishId;
    private String userId;
    private LocalDateTime eatDatetime;
}
