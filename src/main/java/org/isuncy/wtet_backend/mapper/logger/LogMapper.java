package org.isuncy.wtet_backend.mapper.logger;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.isuncy.wtet_backend.entities.pojo.OperationLogger;

@Mapper
public interface LogMapper {
    @Insert("insert into log_operation(id, " +
            "module, log_type, comment, " +
            "user_id, opertime, " +
            "classname, method,ipaddr,request_body,returning,website)" +
            " VALUE (#{id},#{module},#{logType},#{comment},#{userId}," +
            "#{opertime},#{classname},#{method},#{ipaddr},#{requestBody},#{returning},#{website})")
    void logOperation(OperationLogger logger);
}
