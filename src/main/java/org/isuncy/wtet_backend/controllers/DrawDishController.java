package org.isuncy.wtet_backend.controllers;

import com.fasterxml.jackson.databind.Module;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.services.draw.DrawServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/draw")
public class DrawDishController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    DrawServiceI drawService;

    @PostMapping("/random")
    @Operation(summary = "随机抽取菜品")
    @OperLog(module = "菜品抽取", logType = LogType.INFO, comment = "随机抽取菜品")
    public Result<DishGetVO> randomDish(@RequestBody DishSelectDTO dishSelectDTO) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<DishGetVO>().unAuth("未授权");
        }
        return drawService.randomDish(userId, dishSelectDTO);
    }

    @PostMapping("/select")
    @Operation(summary = "确认选择菜品")
    @OperLog(module = "菜品抽取", logType = LogType.INFO, comment = "确认选择菜品")
    public Result<String> selectDishes(@RequestBody String[] dishIds) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("error");
        }
        return drawService.selectDishes(userId, dishIds);
    }

    @GetMapping("/cancel")
    @Operation(summary = "取消选择菜品")
    @OperLog(module = "菜品抽取", logType = LogType.INFO, comment = "取消选择菜品")
    public Result<String> cancelDish(@RequestParam("dishId") String dishId) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("error");
        }
        return drawService.cancelDish(userId, dishId);
    }
}