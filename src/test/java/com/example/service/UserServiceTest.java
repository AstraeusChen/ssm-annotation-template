package com.example.service;

import com.example.config.RootConfig;
import com.example.model.dto.UserCreateDTO;
import com.example.model.dto.UserQueryDTO;
import com.example.model.vo.UserVO;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testQueryPage() {
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(1);
        query.setPageSize(5);
        PageInfo<UserVO> page = userService.queryPage(query);
        assertNotNull(page);
        assertTrue(page.getList().size() > 0);
    }

    @Test
    public void testCreateAndDelete() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("testuser_" + System.currentTimeMillis());
        dto.setPassword("123456");
        dto.setRealName("测试用户");
        dto.setEmail("test@example.com");
        dto.setPhone("13800138005");
        UserVO vo = userService.create(dto);
        assertNotNull(vo.getId());
        // 删除
        userService.delete(vo.getId());
    }
}