package org.zhangrui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园小卖部后端服务启动类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("org.zhangrui.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
