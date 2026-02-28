package org.isuncy.wtet_backend.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EatRecordVO {
    private String dishName;
    private String eatDateTime;
}
