package org.isuncy.wtet_backend.controllers;

import cn.hutool.log.Log;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.AIDishVO;
import org.isuncy.wtet_backend.entities.vo.AILabelVO;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.entities.vo.LabelGetVO;
import org.isuncy.wtet_backend.services.ai.AiServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    HttpServletRequest request;
    @Autowired
    AiServiceI aiService;

    @GetMapping("/tags")
    @Operation(summary = "ai自动填充标签")
    @OperLog(module = "AI", logType = LogType.INFO, comment = "ai自动填充标签")
    public Result<List<AILabelVO>> getTags() {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<List<AILabelVO>>().unAuth("error");
        }
        return aiService.getTags(userId);
    }

    @PostMapping("/dish")
    @Operation(summary = "ai推荐菜品")
    @OperLog(module = "AI", logType = LogType.INFO, comment = "AI推荐菜品")
    public Result<AIDishVO> getDish(@RequestBody DishSelectDTO dishSelectDTO) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<AIDishVO>().unAuth("error");
        }
        return aiService.getDish(dishSelectDTO, userId);
    }

    @GetMapping("/updateTags")
    @Operation(summary = "ai自动为某菜品更新标签")
    @OperLog(module = "AI", logType = LogType.INFO, comment = "AI自动为某菜品更新标签")
    public Result<String> updateTags(@RequestParam("dishId") String dishId) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("error");
        }
        return aiService.updateTags(userId, dishId);
    }

    @PostMapping("/generateTags")
    @Operation(summary = "ai自动为某菜品生成标签")
    @OperLog(module = "AI", logType = LogType.INFO, comment = "AI自动为某菜品生成标签")
    public Result<List<LabelGetVO>> generateTags(@RequestBody DishAddDTO dishAddDTO) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<List<LabelGetVO>>().unAuth("error");
        }
        return aiService.generateTags(userId, dishAddDTO);
    }

    @GetMapping("/summary")
    @Operation(summary = "ai生成分析")
    @OperLog(module = "AI", logType = LogType.INFO, comment = "AI生成分析")
    public Result<String> getAISummary() {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("error");
        }
        return aiService.getAISummary(userId);
    }

}
