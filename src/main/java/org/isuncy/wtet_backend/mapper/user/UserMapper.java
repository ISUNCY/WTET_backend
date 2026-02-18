package org.isuncy.wtet_backend.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.isuncy.wtet_backend.entities.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
