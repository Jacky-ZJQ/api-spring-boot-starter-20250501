package com.jacky.excel.core.config;
import com.jacky.excel.core.domain.IExcelServiceFactory;
import com.jacky.excel.core.domain.process.impl.ExcelImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public IExcelServiceFactory createExcelFactory() {
        return new ExcelImpl();
    }

}