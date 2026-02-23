package org.isuncy.wtet_backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.entities.enums.MealTime;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishAddDTO {
    String dishName;
    Double price;
    String description;
    List<String> labelsId;
}
