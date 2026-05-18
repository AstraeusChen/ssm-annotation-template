package com.example.model.dto;

import lombok.Data;

@Data
public class UserQueryDTO {
    private String username;
    private String realName;
    private Integer status;
    private String email;
    private Integer pageNum = 1;   // 当前页码
    private Integer pageSize = 10; // 每页条数
}