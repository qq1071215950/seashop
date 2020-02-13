package com.haojing;

import com.haojing.fiter.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.haojing.mapper")
@ComponentScan(basePackages = {"com.haojing", "org.n3r.idworker"})
// 扫描所有包以及相关组件包
@EnableScheduling       // 开启定时任务
public class Application {
    // todo token存在一个过期时间，在登录的时候返回一个token，需要保存起来，请求相关接口时，带上token
    // todo 用户模块和管理员添加了登录认证，需要在请求头中加入 k-Authorization v- Bearer+一个空格+token
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
