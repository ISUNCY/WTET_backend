package org.isuncy.wtet_backend.services.ai;

import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.AIDishVO;
import org.isuncy.wtet_backend.entities.vo.AILabelVO;
import org.isuncy.wtet_backend.entities.vo.LabelGetVO;

import java.util.List;

public interface AiServiceI {
    Result<List<AILabelVO>> getTags(String userId);

    Result<AIDishVO> getDish(DishSelectDTO dishSelectDTO, String userId);

    Result<List<LabelGetVO>> generateTags(String userId, DishAddDTO dishSelectDTO);

    Result<String> updateTags(String userId, String dishId);

    Result<String> getAISummary(String userId);
}
