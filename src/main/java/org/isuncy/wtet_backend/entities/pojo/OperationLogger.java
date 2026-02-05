package org.isuncy.wtet_backend.entities.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isuncy.wtet_backend.annotation.LogType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("log_operation")
public class OperationLogger {
    private String id;
    private String module;
    private LogType logType;
    private String comment;
    private String userId;
    private LocalDateTime opertime;
    private String classname;
    private String method;
    private String ipaddr;
    private String requestBody;
    private String returning;
    private Boolean website;
}
