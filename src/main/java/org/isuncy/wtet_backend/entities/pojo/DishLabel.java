package org.isuncy.wtet_backend.entities.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 食物_标签连表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bs_dish_label")
public class DishLabel {
    @TableId
    private String id;
    private String userId;
    private String dishId;
    private String labelId;
}
