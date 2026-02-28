package org.isuncy.wtet_backend.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDataWithLabel {
    String dishName;    //菜品名称
    Double price;       //价格
    String description; //描述
    Integer favourite;      //喜好度
    Integer eatTimes;       //食用次数
    String[] labels;
}
