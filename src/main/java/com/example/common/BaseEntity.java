package com.example.common;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 【企业规范】基础实体类，包含通用字段，供其他实体继承。
 */
@Data
public abstract class BaseEntity {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}