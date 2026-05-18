package com.example.service;

import com.example.model.dto.UserCreateDTO;
import com.example.model.dto.UserQueryDTO;
import com.example.model.dto.UserUpdateDTO;
import com.example.model.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    UserVO create(UserCreateDTO dto);
    void update(UserUpdateDTO dto);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    UserVO getById(Long id);
    PageInfo<UserVO> queryPage(UserQueryDTO queryDTO);
}