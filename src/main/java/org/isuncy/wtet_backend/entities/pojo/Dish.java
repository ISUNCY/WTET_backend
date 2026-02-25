package org.isuncy.wtet_backend.entities.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.entities.enums.MealTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bs_dish")
public class Dish {
    //Dish
    //价格、菜品名称、喜好度、食用次数、描述
    @TableId
    String id;          //id
    String userId;      //用户id
    String dishName;    //菜品名称
    Double price;       //价格
    String description; //描述
    Integer favourite;      //喜好度
    Integer eatTimes;       //食用次数
}
