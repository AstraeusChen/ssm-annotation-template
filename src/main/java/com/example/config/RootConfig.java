package com.example.config;

import com.github.pagehelper.PageInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 【SSM 知识点】根容器配置，负责管理 Service、Mapper、数据源、事务等非 Web 层 Bean。
 * 使用 @PropertySource 加载不同环境的配置文件，通过 @Profile 实现环境隔离。
 */
@Configuration
@ComponentScan(basePackages = {"com.example.service", "com.example.common"})
@PropertySource("classpath:application-${spring.profiles.active:dev}.properties")
@EnableTransactionManagement  // 开启注解驱动事务管理
public class RootConfig {

    @Autowired
    private Environment env;

    /**
     * 配置 HikariCP 数据源（Spring 6 默认推荐）
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(env.getProperty("jdbc.driver"));
        ds.setJdbcUrl(env.getProperty("jdbc.url"));
        ds.setUsername(env.getProperty("jdbc.username"));
        ds.setPassword(env.getProperty("jdbc.password"));
        ds.setMaximumPoolSize(env.getProperty("jdbc.maxPoolSize", Integer.class, 20));
        ds.setMinimumIdle(env.getProperty("jdbc.minIdle", Integer.class, 5));
        return ds;
    }

    /**
     * 配置 SqlSessionFactoryBean，整合 MyBatis
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 1. 设置实体类别名包（已有）
        factoryBean.setTypeAliasesPackage("com.example.model.entity");

        // 2. 配置 PageHelper 分页插件（已有，可保留）
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties props = new Properties();
        props.setProperty("helperDialect", "mysql");
        props.setProperty("reasonable", "true");
        pageInterceptor.setProperties(props);
        factoryBean.setPlugins(new Interceptor[]{pageInterceptor});

        // 3. 开启驼峰命名自动映射（已有）
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);

        // 4. ⭐ 关键补充：指定 Mapper XML 文件的路径
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(
                resolver.getResources("classpath:com/example/mapper/*.xml")
        );

        return factoryBean.getObject();
    }

    /**
     * 配置 MapperScannerConfigurer，扫描所有 Mapper 接口
     */
    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.example.mapper");
        // 指定 sqlSessionFactoryBeanName，避免与 DataSource 初始化顺序冲突
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return configurer;
    }

    /**
     * 事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * BCrypt 密码编码器，用于用户密码加密与校验
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}