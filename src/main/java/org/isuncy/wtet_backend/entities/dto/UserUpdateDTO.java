package org.isuncy.wtet_backend.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String id;
    private String username;
    private String password;
    private Double weight;
    private Double height;
}
