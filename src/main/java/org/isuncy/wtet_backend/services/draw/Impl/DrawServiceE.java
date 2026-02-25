package org.isuncy.wtet_backend.services.draw.Impl;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.DishLabel;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.mapper.dish.DishLabelMapper;
import org.isuncy.wtet_backend.mapper.dish.DishMapper;
import org.isuncy.wtet_backend.mapper.dish.LabelMapper;
import org.isuncy.wtet_backend.services.dish.DishServiceI;
import org.isuncy.wtet_backend.services.draw.DrawServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrawServiceE implements DrawServiceI {
    @Autowired
    private DishServiceI dishService;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public Result<DishGetVO> randomDish(String userId, DishSelectDTO dishSelectDTO) {
        List<DishGetVO> dishList = dishService.selectDishes(userId, dishSelectDTO).getReceive();
        if (dishList.isEmpty()) {
            return new Result<DishGetVO>().error("No dish selected");
        }
        List<WeightRandom.WeightObj<DishGetVO>> weightList = new ArrayList<>();
        for (DishGetVO dish : dishList) {
            weightList.add(new WeightRandom.WeightObj<DishGetVO>(dish, dish.getFavourite()));
        }
        DishGetVO res = RandomUtil.weightRandom(weightList).next();
        return new Result<DishGetVO>().success(res);
    }

    @Override
    public Result<String> selectDishes(String userId, String[] dishIds) {
        List<Dish> dishes = dishMapper.selectList(new QueryWrapper<Dish>().eq("user_id", userId).in("dish_id", (Object) dishIds));
        for (Dish dish : dishes) {
            if (dish.getFavourite() < 20) {
                dish.setFavourite(dish.getFavourite() + 1);
                dish.setEatTimes(dish.getEatTimes() + 1);
                dishMapper.updateById(dish);
            }
        }
        return new Result<String>().success();
    }

    @Override
    public Result<String> cancelDish(String userId, String dishId) {
        Dish dish = dishMapper.selectOne(new QueryWrapper<Dish>().eq("user_id", userId).eq("id", dishId));
        if (dish.getFavourite() > 1) {
            dish.setFavourite(dish.getFavourite() - 1);
            dishMapper.updateById(dish);
        }
        return new Result<String>().success();
    }
}