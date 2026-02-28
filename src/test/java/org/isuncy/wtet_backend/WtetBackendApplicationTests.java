package org.isuncy.wtet_backend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.isuncy.wtet_backend.entities.pojo.Dish;
import org.isuncy.wtet_backend.entities.pojo.DishLabel;
import org.isuncy.wtet_backend.entities.pojo.Label;
import org.isuncy.wtet_backend.entities.vo.DishDataWithLabel;
import org.isuncy.wtet_backend.entities.vo.DishImportVO;
import org.isuncy.wtet_backend.mapper.dish.DishLabelMapper;
import org.isuncy.wtet_backend.mapper.dish.DishMapper;
import org.isuncy.wtet_backend.mapper.dish.LabelMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WtetBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    DishMapper dishMapper;
    @Autowired
    LabelMapper labelMapper;
    @Autowired
    DishLabelMapper dishLabelMapper;

    @Test
    void importDishLabel() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:static/dishDataWithLabel.json");
        JSON json = JSONUtil.readJSON(file, Charset.defaultCharset());
        String jsonString = json.toStringPretty();
        List<DishDataWithLabel> dishImportVOS = JSONUtil.toList(jsonString, DishDataWithLabel.class);
        for (DishDataWithLabel d : dishImportVOS) {
            Dish dish = dishMapper.selectOne(new QueryWrapper<Dish>().eq("user_id", "Default").eq("dish_name", d.getDishName()));
            for (String label : d.getLabels()) {
                Label lb = labelMapper.selectOne(new QueryWrapper<Label>().eq("user_id", "Default").eq("label_name", label));
                DishLabel dishLabel = new DishLabel();
                dishLabel.setId(UUID.randomUUID().toString());
                dishLabel.setDishId(dish.getId());
                dishLabel.setUserId("Default");
                dishLabel.setLabelId(lb.getId());
                dishLabelMapper.insert(dishLabel);
            }
        }
    }

//    @Test
//    void ImportDefaultData() throws FileNotFoundException {
//        File file = ResourceUtils.getFile("classpath:static/dishData.json");
//        JSON json = JSONUtil.readJSON(file, Charset.defaultCharset());
//        String jsonString = json.toStringPretty();
//        List<DishImportVO> dishImportVOS = JSONUtil.toList(jsonString, DishImportVO.class);
//        for (DishImportVO dishImportVO : dishImportVOS) {
//            System.out.println(dishImportVO);
//            Dish dish = Convert.convert(Dish.class, dishImportVO);
//            dish.setId(UUID.randomUUID().toString());
//            dish.setUserId("Default");
//            dishMapper.insert(dish);
//        }
//    }
//    @Test
//    void ImportLabelData() {
//        List<String> tags = Arrays.asList(
//                "炒菜",
//                "川菜",
//                "晚餐",
//                "炖菜",
//                "高蛋白",
//                "家常菜",
//                "辣",
//                "清淡",
//                "酸",
//                "素食",
//                "汤羹",
//                "甜",
//                "午餐",
//                "下饭菜",
//                "主食"
//        );
//        for (String tag : tags) {
//            Label label = new Label();
//            label.setId(UUID.randomUUID().toString());
//            label.setLabelName(tag);
//            label.setUserId("Default");
//            labelMapper.insert(label);
//            System.out.println(label.getId()+":"+label.getLabelName());
//        }
//    }
//
//    @Test
//    void delDefault() {
//        dishMapper.delete(new QueryWrapper<Dish>().eq("user_id", "Default"));
//    }

}
