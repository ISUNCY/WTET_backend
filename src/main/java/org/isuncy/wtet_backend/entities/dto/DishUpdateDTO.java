package org.isuncy.wtet_backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.entities.enums.MealTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateDTO {
    String dishName;
    String id;
    Double price;
    String description;
    MealTime[] mealTimes;
}
