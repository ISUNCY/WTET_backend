package org.isuncy.wtet_backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.entities.enums.MealTime;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateDTO {
    String id;          //id
    String dishName;    //菜品名称
    Double price;       //价格
    String description; //描述
    Integer favourite;      //喜好度
    Integer eatTimes;       //食用次数
    List<String> labelsId;
}
