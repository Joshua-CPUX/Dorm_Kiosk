package org.zhangrui;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot 应用上下文测试
 */
@SpringBootTest
class ServerApplicationTests {

    @Test
    @DisplayName("测试应用上下文加载")
    void contextLoads() {
        assertTrue(true, "应用上下文加载成功");
    }

    @Test
    @DisplayName("测试应用启动")
    void applicationStarts() {
        assertTrue(true, "应用启动成功");
    }
}
