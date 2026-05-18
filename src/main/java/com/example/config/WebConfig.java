package com.example.config;

import com.example.common.interceptor.TraceIdInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

/**
 * 【SSM 知识点】SpringMVC 子容器配置，仅扫描 Controller 相关组件。
 * 启用 @EnableWebMvc 完全接管 MVC 配置。
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example.controller",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class))
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TraceIdInterceptor traceIdInterceptor;

//    /**
//     * 静态资源处理：将 /static/** 映射到 /webapp/static/ 目录
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("/static/");
//        // SpringDoc 需要的静态资源
//        registry.addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 让 Spring MVC 可以处理 webapp 下的静态资源
        registry.addResourceHandler("/**")
                .addResourceLocations("/");
       registry.addResourceHandler("/swagger-ui/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    }

    /**
     * 全局 CORS 跨域配置（前后端分离必须）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 注册拦截器（全链路 TraceId 注入）
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor)
                .addPathPatterns("/api/**");
    }

    /**
     * 配置 JSON 消息转换器，支持 Java 8 时间 API 序列化
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        converter.setObjectMapper(mapper);
        converters.add(converter);
    }

    /**
     * 视图解析器（虽然前后端分离，但保留用于可能的 JSP 或默认转发）
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 访问根路径时直接转发到 a.html
        registry.addViewController("/").setViewName("forward:/a.html");
    }



}