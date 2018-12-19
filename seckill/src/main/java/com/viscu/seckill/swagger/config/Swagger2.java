package com.viscu.seckill.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ Create by ostreamBaba on 18-12-19
 * @ 描述
 */


@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi(){
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sam 项目档案接口")
                .description("Magical Sam 项目的接口文档，符合RESTful API。")
                .version("1.0")
                .build();
        return new Docket( DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.viscu.seckill.swagger.controller")) //对包进行扫描
                .paths( PathSelectors.any())
                .build();
    }



}
