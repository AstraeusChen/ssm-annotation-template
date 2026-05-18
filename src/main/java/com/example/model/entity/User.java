package com.example.model.entity;

import com.example.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private Integer status;  // 0:禁用 1:启用
}