package org.isuncy.wtet_backend.entities.statics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageList<T> {
    private int page;
    private int size;
    private int total;
    private List<T> data;
}
