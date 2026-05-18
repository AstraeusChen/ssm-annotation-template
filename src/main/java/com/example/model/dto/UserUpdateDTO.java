package com.example.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserUpdateDTO extends UserCreateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;
}