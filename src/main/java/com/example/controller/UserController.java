package com.example.controller;

import com.example.common.Result;
import com.example.common.annotation.WebLog;
import com.example.model.dto.UserCreateDTO;
import com.example.model.dto.UserQueryDTO;
import com.example.model.dto.UserUpdateDTO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "用户管理", description = "用户增删改查、分页、批量删除")
@RestController
@RequestMapping("/api/users")
@Validated  // 开启方法级别的校验（如 @NotNull 在 @PathVariable 上）
public class UserController {
//http://localhost:8080/ssm-annotation-template/index.html
    @Autowired
    private UserService userService;

    @Operation(summary = "新增用户")
    @WebLog("新增用户")
    @PostMapping
    public Result<UserVO> create(@Valid @RequestBody UserCreateDTO dto) {
        UserVO vo = userService.create(dto);
        return Result.success(vo);
    }

    @Operation(summary = "修改用户")
    @WebLog("修改用户")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable @NotNull Long id, @Valid @RequestBody UserUpdateDTO dto) {
        dto.setId(id);
        userService.update(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "删除用户")
    @WebLog("删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @NotNull Long id) {
        userService.delete(id);
        return Result.success("删除成功", null);
    }

    @Operation(summary = "批量删除用户")
    @WebLog("批量删除用户")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        userService.deleteBatch(ids);
        return Result.success("批量删除成功", null);
    }

    @Operation(summary = "查询用户详情")
    @WebLog("查询用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable @NotNull Long id) {
        UserVO vo = userService.getById(id);
        return Result.success(vo);
    }

    @Operation(summary = "分页查询用户列表")
    @WebLog("分页查询用户")
    @GetMapping("/page")
    public Result<PageInfo<UserVO>> queryPage(@Valid UserQueryDTO queryDTO) {
        PageInfo<UserVO> page = userService.queryPage(queryDTO);
        return Result.success(page);
    }
}