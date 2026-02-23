package org.isuncy.wtet_backend.entities.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bs_label")
public class Label {
    @TableId
    private String id;
    private String userId;
    private String labelName;
}
