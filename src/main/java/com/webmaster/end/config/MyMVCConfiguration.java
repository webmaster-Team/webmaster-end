package com.webmaster.end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMVCConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        MyInterceptor myInterceptor = new MyInterceptor();
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //所有方法
                .allowedOrigins("*") //允许的域名
                .allowCredentials(true)
                .allowedHeaders("*") // 允许请求头
                .allowedMethods("GET", "POST", "DELETE", "PUT","OPTIONS")//允许方法
                .maxAge(3600); //表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
    }
}
