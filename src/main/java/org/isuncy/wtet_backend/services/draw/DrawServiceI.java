package org.isuncy.wtet_backend.services.draw;

import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;


public interface DrawServiceI {

    Result<DishGetVO> randomDish(String userId, DishSelectDTO dishSelectDTO);

    Result<String> selectDishes(String userId, String[] dishIds);

    Result<String> cancelDish(String userId, String dishId);
}
