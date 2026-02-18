package org.isuncy.wtet_backend.entities.statics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer state;          //响应码
    private String description;     //响应信息
    private T receive;              //返回的数据

    //增删改 成功响应
    public Result<T> success() {
        return new Result<T>(200, "success", null);
    }

    //查询 成功响应
    public Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }

    //失败响应
    public Result<T> error(String msg) {
        return new Result(400, msg, "CLIENT_OPERATION_ERROR");
    }

    //令牌过期
    public Result<T> unAuth(String msg) {
        return new Result(401, msg, "Authorization invalid.");
    }

    //服务器内部错误
    public Result<T> serverError(String description) {
        return new Result(500, description, "SERVER_INTERNAL_ERROR");
    }
}
