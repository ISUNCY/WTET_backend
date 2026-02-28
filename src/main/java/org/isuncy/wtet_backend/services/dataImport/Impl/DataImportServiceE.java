package org.isuncy.wtet_backend.services.dataImport.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.DishLabel;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.pojo.User;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.mapper.dish.DishLabelMapper;
import org.isuncy.wtet_backend.mapper.dish.DishMapper;
import org.isuncy.wtet_backend.mapper.dish.LabelMapper;
import org.isuncy.wtet_backend.mapper.user.UserMapper;
import org.isuncy.wtet_backend.services.dataImport.DataImportServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

@Service
public class DataImportServiceE implements DataImportServiceI {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishLabelMapper dishLabelMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> importDefaultData(String userId) {
        User user = userMapper.selectById(userId);
        if (user.isDataImport()) {
            return new Result<String>().error("已导入过相关菜品");
        }
        List<DishLabel> dishLabelList = dishLabelMapper.selectList(new QueryWrapper<DishLabel>().eq("user_id", "Default"));
        for (DishLabel dishLabel : dishLabelList) {
            Dish dish = dishMapper.selectOne(new QueryWrapper<Dish>().eq("id", dishLabel.getDishId()));
            Label label = labelMapper.selectOne(new QueryWrapper<Label>().eq("id", dishLabel.getLabelId()));
            Dish dish1 = dishMapper.selectOne(new QueryWrapper<Dish>().eq("user_id", userId).eq("dish_name", dish.getDishName()));
            Label label1 = labelMapper.selectOne(new QueryWrapper<Label>().eq("user_id", userId).eq("label_name", label.getLabelName()));
            if (dish1 == null) {
                dish.setId(UUID.randomUUID().toString());
                dish.setUserId(userId);
                dishMapper.insert(dish);
            }
            else {
                dish = dish1;
            }
            if (label1 == null) {
                label.setId(UUID.randomUUID().toString());
                label.setUserId(userId);
                labelMapper.insert(label);
            }
            else {
                label = label1;
            }
            dishLabel.setLabelId(label.getId());
            dishLabel.setUserId(userId);
            dishLabel.setDishId(dish.getId());
            dishLabel.setId(UUID.randomUUID().toString());
            dishLabelMapper.insert(dishLabel);
        }
        user.setDataImport(true);
        userMapper.updateById(user);
        return new Result<String>().success();
    }
}
