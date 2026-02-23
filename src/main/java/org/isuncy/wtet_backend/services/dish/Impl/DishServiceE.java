package org.isuncy.wtet_backend.services.dish.Impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.dto.DishUpdateDTO;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.DishLabel;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.mapper.dish.DishLabelMapper;
import org.isuncy.wtet_backend.mapper.dish.DishMapper;
import org.isuncy.wtet_backend.mapper.dish.LabelMapper;
import org.isuncy.wtet_backend.services.dish.DishServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceE implements DishServiceI {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private DishLabelMapper dishLabelMapper;

    @Override
    public Result<String> addLabel(String userId, String labelName) {
        boolean exist = labelMapper.exists(new QueryWrapper<Label>().eq("label_name", labelName).eq("user_id", userId));
        if (exist) {
            return new Result<String>().error("已存在此标签");
        }
        Label label = new Label();
        label.setLabelName(labelName);
        label.setUserId(userId);
        label.setId(UUID.randomUUID().toString());
        labelMapper.insert(label);
        return new Result<String>().success();
    }

    @Override
    public Result<List<Label>> getLabelList(String userId) {
        List<Label> list =  labelMapper.selectList(new QueryWrapper<Label>().eq("user_id", userId));
        return new Result<List<Label>>().success(list);
    }

    @Override
    public Result<String> deleteLabel(String labelId) {
        labelMapper.deleteById(labelId);
        dishLabelMapper.delete(new QueryWrapper<DishLabel>().eq("label_id", labelId));
        return new Result<String>().success();
    }

    @Override
    public Result<String> addDish(String userId, DishAddDTO dishAddDTO) {
        Dish dish = new Dish();
        dish.setDishName(dishAddDTO.getDishName());
        dish.setUserId(userId);
        dish.setId(UUID.randomUUID().toString());
        dish.setDescription(dishAddDTO.getDescription());
        dish.setPrice(dishAddDTO.getPrice());
        dish.setFavourite(10);
        dish.setEatTimes(0);
        dishMapper.insert(dish);
        for (String labelId : dishAddDTO.getLabelsId()) {
            DishLabel dishLabel = new DishLabel();
            dishLabel.setDishId(dish.getId());
            dishLabel.setLabelId(labelId);
            dishLabel.setUserId(userId);
            dishLabel.setId(UUID.randomUUID().toString());
            dishLabelMapper.insert(dishLabel);
        }
        return new Result<String>().success();
    }

    @Override
    public Result<String> updateDish(String userId, DishUpdateDTO dishUpdateDTO) {
        Dish dish = dishMapper.selectById(dishUpdateDTO.getId());
        if (dish == null) {
            return new Result<String>().error("<UNK>");
        }
        dish = Convert.convert(Dish.class, dishUpdateDTO);
        dishMapper.updateById(dish);
        dishLabelMapper.delete(new QueryWrapper<DishLabel>().eq("dish_id", dish.getId()).eq("user_id", userId));
        for (String labelId : dishUpdateDTO.getLabelsId()) {
            DishLabel dishLabel = new DishLabel();
            dishLabel.setDishId(dish.getId());
            dishLabel.setLabelId(labelId);
            dishLabel.setUserId(userId);
            dishLabel.setId(UUID.randomUUID().toString());
            dishLabelMapper.insert(dishLabel);
        }
        return new Result<String>().success();
    }

    @Override
    public Result<String> deleteDish(String dishId) {
        dishMapper.deleteById(dishId);
        dishLabelMapper.delete(new QueryWrapper<DishLabel>().eq("dish_id", dishId));
        return new Result<String>().success();
    }

    @Override
    public Result<DishGetVO> getSingleDish(String userId, String dishId) {
        Dish dish = dishMapper.selectOne(new QueryWrapper<Dish>().eq("dish_id", dishId).eq("user_id", userId));
        if (dish == null) {
            return new Result<DishGetVO>().error("<UNK>");
        }
        DishGetVO dishGetVO = Convert.convert(DishGetVO.class, dish);
        dishGetVO.setLabels(getDishLabels(userId, dishId));
        return new Result<DishGetVO>().success(dishGetVO);
    }

    private List<Label> getDishLabels(String userId, String dishId) {
        List<DishLabel> dishLabelList = dishLabelMapper.selectList(new QueryWrapper<DishLabel>().eq("user_id", userId).eq("dish_id", dishId));
        List<Label> labels = new ArrayList<>();
        for (DishLabel dishLabel : dishLabelList) {
            Label label = labelMapper.selectById(dishLabel.getLabelId());
            if (label == null) continue;
            labels.add(label);
        }
        return labels;
    }

    @Override
    public Result<List<DishGetVO>> selectDishes(String userId, DishSelectDTO dishSelectDTO) {
        List<DishLabel> dishLabelList = dishLabelMapper.selectList(
                new QueryWrapper<DishLabel>()
                        .eq("user_id", userId)
                        .in(!dishSelectDTO.getLabelId().isEmpty(), "dish_id", dishSelectDTO.getLabelId())
        );
        List<String> dishIds = new ArrayList<>();
        for (DishLabel dishLabel : dishLabelList) {
            dishIds.add(dishLabel.getDishId());
        }
        List<Dish> dishes = dishMapper.selectList(
                new QueryWrapper<Dish>()
                        .eq("user_id", userId)
                        .eq(!dishSelectDTO.getLabelId().isEmpty(), "dish_id", dishIds)
                        .like(StringUtils.hasLength(dishSelectDTO.getDishName()), "dish_name", dishSelectDTO.getDishName())
                        .ge(dishSelectDTO.getPrice_low() != null, "dish_price_low", dishSelectDTO.getPrice_low())
                        .le(dishSelectDTO.getPrice_high() != null, "dish_price_high", dishSelectDTO.getPrice_high())
        );
        List<DishGetVO> dishGetVOs = new ArrayList<>();
        for (Dish dish : dishes) {
            DishGetVO dishGetVO = Convert.convert(DishGetVO.class, dish);
            dishGetVO.setLabels(getDishLabels(userId, dish.getId()));
            dishGetVOs.add(dishGetVO);
        }
        return new Result<List<DishGetVO>>().success(dishGetVOs);
    }
}
