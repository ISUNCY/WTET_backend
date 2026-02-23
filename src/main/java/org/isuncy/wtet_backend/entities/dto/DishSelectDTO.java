package org.isuncy.wtet_backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishSelectDTO {
    String id;          //id
    String dishName;    //菜品名称
    Double price_low;       //价格
    Double price_high;
    List<String> labelId;
}
