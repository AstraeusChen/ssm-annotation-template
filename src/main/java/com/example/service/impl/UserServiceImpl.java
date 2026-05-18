package com.example.service.impl;

import com.example.common.exception.BusinessException;
import com.example.mapper.UserMapper;
import com.example.model.dto.UserCreateDTO;
import com.example.model.dto.UserQueryDTO;
import com.example.model.dto.UserUpdateDTO;
import com.example.model.entity.User;
import com.example.model.vo.UserVO;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserVO create(UserCreateDTO dto) {
        // 校验用户名唯一性（略，可自行扩展）
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        // 密码加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insert(user);
        return convertToVO(user);
    }

    @Override
    public void update(UserUpdateDTO dto) {
        User exist = userMapper.selectById(dto.getId());
        if (exist == null) {
            throw new BusinessException(404, "用户不存在");
        }
        BeanUtils.copyProperties(dto, exist);
        // 如果密码被修改且不为空，则加密
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            exist.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userMapper.updateById(exist);
    }

    @Override
    public void delete(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        userMapper.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的用户");
        }
        userMapper.deleteBatchByIds(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<UserVO> queryPage(UserQueryDTO queryDTO) {
        // 启动分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<User> users = userMapper.selectPageByCondition(queryDTO);
        // 转换为 VO
        List<UserVO> voList = users.stream().map(this::convertToVO).collect(Collectors.toList());
        PageInfo<UserVO> pageInfo = new PageInfo<>(voList);
        // PageInfo 内部已包含分页信息，直接返回即可
        return pageInfo;
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}