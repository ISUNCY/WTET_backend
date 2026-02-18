package org.isuncy.wtet_backend.entities.pojo;

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
    String dishName;
    String id;
    Double price;
    String description;
    MealTime[] mealTimes;
}
