package com.example.mapper;

import com.example.model.dto.UserQueryDTO;
import com.example.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 【SSM 知识点】MyBatis Mapper 接口，单表 CRUD 使用注解，复杂查询/动态 SQL 使用 XML。
 */
@Mapper
public interface UserMapper {

    // ========== 注解方式（简单 CRUD）==========
    @Insert("INSERT INTO sys_user(username, password, real_name, email, phone, status, create_time, update_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{email}, #{phone}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE sys_user SET username=#{username}, real_name=#{realName}, email=#{email}, " +
            "phone=#{phone}, status=#{status}, update_time=NOW() WHERE id=#{id}")
    int updateById(User user);

    @Select("SELECT id, username, password, real_name, email, phone, status, create_time, update_time " +
            "FROM sys_user WHERE id = #{id}")
    User selectById(Long id);

    // 批量删除（XML 实现动态 SQL）
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    // 分页查询 + 条件查询（XML 实现动态 SQL）
    List<User> selectPageByCondition(UserQueryDTO queryDTO);
}