package com.jacky.excel.core.config;
import com.jacky.excel.core.domain.IExcelServiceFactory;
import com.jacky.excel.core.domain.process.impl.ExcelImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot自动配置类，自动注册Excel服务工厂实例
 */
@Configuration
public class ExcelAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public IExcelServiceFactory createExcelFactory() {
        return new ExcelImpl();
    }

}