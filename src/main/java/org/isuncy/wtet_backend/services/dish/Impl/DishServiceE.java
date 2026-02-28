package org.isuncy.wtet_backend.services.dish.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.isuncy.wtet_backend.entities.dto.DishAddDTO;
import org.isuncy.wtet_backend.entities.dto.DishSelectDTO;
import org.isuncy.wtet_backend.entities.dto.DishUpdateDTO;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.DishLabel;
import org.isuncy.wtet_backend.entities.pojo.EatRecord;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.statics.PageList;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.entities.vo.DishGetVO;
import org.isuncy.wtet_backend.entities.vo.EatRecordVO;
import org.isuncy.wtet_backend.entities.vo.LabelGetVO;
import org.isuncy.wtet_backend.mapper.dish.DishLabelMapper;
import org.isuncy.wtet_backend.mapper.dish.DishMapper;
import org.isuncy.wtet_backend.mapper.dish.EatRecordMapper;
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
    @Autowired
    private EatRecordMapper eatRecordMapper;

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
    public Result<List<LabelGetVO>> getLabelList(String userId) {
        List<Label> list =  labelMapper.selectList(new QueryWrapper<Label>().eq("user_id", userId));
        List<LabelGetVO> voList = new ArrayList<>();
        for (Label label : list) {
            LabelGetVO vo = new LabelGetVO();
            vo.setId(label.getId());
            vo.setLabelName(label.getLabelName());
            voList.add(vo);
        }

        return new Result<List<LabelGetVO>>().success(voList);
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
        if (dishAddDTO.getPrice() != null) {
            dish.setPrice(dishAddDTO.getPrice());
        }
        else {
            dish.setPrice(0.0);
        }
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
            return new Result<String>().error("error");
        }
        if (dishUpdateDTO.getFavourite() != null) {
            if (dishUpdateDTO.getFavourite() < 1) {
                return new Result<String>().error("喜好度不可小于1");
            }
            if (dishUpdateDTO.getFavourite() > 20) {
                return new Result<String>().error("喜好度不可大于20");
            }
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
            return new Result<DishGetVO>().error("error");
        }
        DishGetVO dishGetVO = Convert.convert(DishGetVO.class, dish);
        dishGetVO.setLabels(getDishLabels(userId, dishId));
        return new Result<DishGetVO>().success(dishGetVO);
    }

    private List<LabelGetVO> getDishLabels(String userId, String dishId) {
        List<DishLabel> dishLabelList = dishLabelMapper.selectList(new QueryWrapper<DishLabel>().eq("user_id", userId).eq("dish_id", dishId));
        List<LabelGetVO> labels = new ArrayList<>();
        for (DishLabel dishLabel : dishLabelList) {
            Label label = labelMapper.selectById(dishLabel.getLabelId());
            if (label == null) continue;
            labels.add(new LabelGetVO(dishLabel.getLabelId(), label.getLabelName()));
        }
        return labels;
    }

    @Override
    public Result<List<DishGetVO>> selectDishes(String userId, DishSelectDTO dishSelectDTO) {
        List<DishLabel> dishLabelList = dishLabelMapper.selectList(
                new QueryWrapper<DishLabel>()
                        .eq("user_id", userId)
                        .in(!dishSelectDTO.getLabelId().isEmpty(), "label_id", dishSelectDTO.getLabelId())
        );
        if (dishLabelList.isEmpty()) {
            return new Result<List<DishGetVO>>().success(new ArrayList<>());
        }
        List<String> dishIds = new ArrayList<>();
        for (DishLabel dishLabel : dishLabelList) {
            dishIds.add(dishLabel.getDishId());
        }
        List<Dish> dishes = dishMapper.selectList(
                new QueryWrapper<Dish>()
                        .eq("user_id", userId)
                        .in(!dishSelectDTO.getLabelId().isEmpty(), "id", dishIds.toArray())
                        .like(StringUtils.hasLength(dishSelectDTO.getDishName()), "dish_name", dishSelectDTO.getDishName())
                        .ge(dishSelectDTO.getPrice_low() != null, "price", dishSelectDTO.getPrice_low())
                        .le(dishSelectDTO.getPrice_high() != null, "price", dishSelectDTO.getPrice_high())
        );
        List<DishGetVO> dishGetVOs = new ArrayList<>();
        for (Dish dish : dishes) {
            DishGetVO dishGetVO = Convert.convert(DishGetVO.class, dish);
            dishGetVO.setLabels(getDishLabels(userId, dish.getId()));
            dishGetVOs.add(dishGetVO);
        }
        return new Result<List<DishGetVO>>().success(dishGetVOs);
    }

    @Override
    public Result<String> updateLabel(String userId, String labelId, String labelName) {
        if (!StringUtils.hasLength(labelId)) {
            return new Result<String>().error("id is empty");
        }
        Label label = new Label(labelId, userId, labelName);
        labelMapper.updateById(label);
        return new Result<String>().success();
    }

    @Override
    public Result<List<EatRecordVO>> getEatRecords(String userId) {
        List<EatRecord> eatRecords = eatRecordMapper.selectList(new QueryWrapper<EatRecord>().eq("user_id", userId));
        List<EatRecordVO> eatRecordVOs = new ArrayList<>();
        for (EatRecord eatRecord : eatRecords) {
            Dish dish = dishMapper.selectById(eatRecord.getDishId() );
            if (dish == null) continue;
            EatRecordVO eatRecordVO = new EatRecordVO();
            eatRecordVO.setDishName(dish.getDishName());
            eatRecordVO.setEatDateTime(eatRecord.getEatDatetime().toString());
            eatRecordVOs.add(eatRecordVO);
        }
        return new Result<List<EatRecordVO>>().success(eatRecordVOs);
    }

}
