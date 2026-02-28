package org.isuncy.wtet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.dto.DishUpdateDTO;
import org.isuncy.wtet_backend.entities.dto.LabelUpdateDTO;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.statics.PageList;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.entities.vo.EatRecordVO;
import org.isuncy.wtet_backend.entities.vo.LabelGetVO;
import org.isuncy.wtet_backend.services.dish.DishServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    @Autowired
    DishServiceI dishService;
    @Autowired
    HttpServletRequest request;

    @PostMapping("/add")
    @Operation(summary = "添加菜品")
    @OperLog(module = "菜品", logType = LogType.INFO, comment = "添加菜品")
    public Result<String> addDish(@RequestBody DishAddDTO dishAddDTO) {
        String userId = JwtHelper.getUserId(request);
        if (!StringUtils.hasLength(userId)) {
            return new Result<String>().unAuth("用户不存在");
        }
        return dishService.addDish(userId, dishAddDTO);
    }

    @GetMapping("/del")
    @Operation(summary = "删除菜品")
    @OperLog(module = "菜品", logType = LogType.INFO, comment = "添加菜品")
    public Result<String> delDish(@RequestParam("dishId") String dishId) {
        String userId = JwtHelper.getUserId(request);
        if (!StringUtils.hasLength(userId)) {
            return new Result<String>().unAuth("error");
        }
        return dishService.deleteDish(dishId);
    }

    @PostMapping("/update")
    @Operation(summary = "更新菜品")
    @OperLog(module = "菜品", logType = LogType.INFO, comment = "更新菜品信息")
    public Result<String> updateDish(@RequestBody DishUpdateDTO dishUpdateDTO) {
        String userId = JwtHelper.getUserId(request);
        if (!StringUtils.hasLength(userId)) {
            return new Result<String>().unAuth("error");
        }
        return dishService.updateDish(userId, dishUpdateDTO);
    }

    @GetMapping("/get/single")
    @Operation(summary = "获取单个菜品信息")
    @OperLog(module = "菜品", logType = LogType.INFO, comment = "获取单个菜品信息")
    public Result<DishGetVO> getSingleDish(@RequestParam("dishId") String dishId) {
        String userId = JwtHelper.getUserId(request);
        if (!StringUtils.hasLength(userId)) {
            return new Result<DishGetVO>().unAuth("error");
        }
        return dishService.getSingleDish(userId, dishId);
    }

    @PostMapping("/get/list")
    @Operation(summary = "获取菜品信息列表")
    @OperLog(module = "菜品", logType = LogType.INFO, comment = "获取菜品信息列表")
    public Result<List<DishGetVO>> getDishList(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestBody DishSelectDTO dishSelectDTO) {
        String userId = JwtHelper.getUserId(request);
        if (!StringUtils.hasLength(userId)) {
            return new Result<List<DishGetVO>>().unAuth("error");
        }
        return dishService.selectDishes(userId, dishSelectDTO);
    }

    @GetMapping("/label/add")
    @Operation(summary = "添加标签")
    @OperLog(module = "菜品标签", logType = LogType.INFO, comment = "添加菜品标签")
    public Result<String> addLabel(@RequestParam("labelName") String labelName) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("用户不存在");
        }
        if (!StringUtils.hasLength(labelName)) {
            return new Result<String>().error("字段不可为空");
        }
        return dishService.addLabel(userId, labelName);
    }

    @GetMapping("/label/getlist")
    @Operation(summary = "获取标签名称列表")
    @OperLog(module = "菜品标签", logType = LogType.INFO, comment="获取菜品标签列表")
    public Result<List<LabelGetVO>> getLabelList() {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<List<LabelGetVO>>().unAuth("用户不存在");
        }
        return dishService.getLabelList(userId);
    }

    @GetMapping("/label/del")
    @Operation(summary = "删除标签")
    @OperLog(module = "菜品标签", logType = LogType.INFO, comment = "删除标签")
    public Result<String> deleteLabel(@RequestParam("labelId") String labelId) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("用户不存在");
        }
        return dishService.deleteLabel(labelId);
    }

    @PostMapping("/label/update")
    @Operation(summary = "更新标签")
    @OperLog(module = "菜品标签", logType = LogType.INFO, comment = "更新标签")
    public Result<String> deleteLabel(@RequestBody LabelUpdateDTO labelUpdateDTO) {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<String>().unAuth("error");
        }
        return dishService.updateLabel(userId, labelUpdateDTO.getLabelId(), labelUpdateDTO.getLabelName());
    }

    @GetMapping("/eatRecord")
    @Operation(summary = "饮食数据")
    @OperLog(module = "饮食数据", logType = LogType.INFO, comment = "饮食数据")
    public Result<List<EatRecordVO>> getEatRecord() {
        String userId = JwtHelper.getUserId(request);
        if (userId == null) {
            return new Result<List<EatRecordVO>>().unAuth("error");
        }
        return dishService.getEatRecords(userId);
    }


}
