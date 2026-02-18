package org.isuncy.wtet_backend.entities.enums;

import lombok.Getter;

@Getter
public enum MealTime {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),;
    private final String mealTime;
    MealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
