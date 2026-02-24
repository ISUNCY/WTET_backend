package org.isuncy.wtet_backend.services.dish;

import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.dto.DishUpdateDTO;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.entities.vo.LabelGetVO;

import java.util.List;

public interface DishServiceI {
    Result<String> addLabel(String userId, String labelName);

    Result<List<LabelGetVO>> getLabelList(String userId);

    Result<String> deleteLabel(String labelId);

    Result<String> addDish(String userId, DishAddDTO dishAddDTO);

    Result<String> updateDish(String userId, DishUpdateDTO dishUpdateDTO);

    Result<String> deleteDish(String dishId);

    Result<DishGetVO> getSingleDish(String userId, String dishId);

    Result<List<DishGetVO>> selectDishes(String userId, DishSelectDTO dishSelectDTO);

    Result<String> updateLabel(String userId, String labelId, String labelName);
}
