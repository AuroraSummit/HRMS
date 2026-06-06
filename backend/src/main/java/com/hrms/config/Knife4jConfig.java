package com.hrms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HRMS 人力资源管理系统 API")
                        .version("1.0.0")
                        .description("HRMS 后端接口文档")
                        .contact(new Contact()
                                .name("HRMS Team")
                                .email("support@hrms.com")));
    }
}
