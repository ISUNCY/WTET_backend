package org.isuncy.wtet_backend.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.entities.pojo.Label;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishGetVO {
    String id;
    String dishName;    //菜品名称
    Double price;       //价格
    String description; //描述
    int favourite;      //喜好度
    int eatTimes;       //食用次数
    List<LabelGetVO> labels;
}
