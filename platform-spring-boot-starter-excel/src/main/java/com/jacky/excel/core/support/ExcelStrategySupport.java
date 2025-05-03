package com.jacky.excel.core.support;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jacky.excel.core.annotion.StrategyModel;
import com.jacky.excel.core.domain.strategy.IExcelStrategyProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class ExcelStrategySupport implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(ExcelStrategySupport.class);

    private ApplicationContext applicationContext;

    /**
     * 导入策略配置组
     */
    private static final ConcurrentHashMap<String, IExcelStrategyProcess> IMPORT_EXCEL_STRATEGY_MAP = new ConcurrentHashMap<>();

    public ExcelStrategySupport() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.logger.info("Spring container start Done load excel strategy Ing...");
            Map<String, Object> strategyModelMap = this.applicationContext.getBeansWithAnnotation(StrategyModel.class);

            for(Map.Entry<String, Object> entry : strategyModelMap.entrySet()) {
                StrategyModel strategyModel = AnnotationUtil.getAnnotation(entry.getValue().getClass(), StrategyModel.class);
                if (entry.getValue() instanceof IExcelStrategyProcess) {
                    IMPORT_EXCEL_STRATEGY_MAP.put(strategyModel.strategyName(), (IExcelStrategyProcess)entry.getValue());
                }
            }

            this.logger.info("Spring container  load strategy Done:{}", JSON.toJSON(IMPORT_EXCEL_STRATEGY_MAP));
        } catch (Exception e) {
            this.logger.error("Spring container load strategy Failed:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected IExcelStrategyProcess getImportStrategy(String strategyName) {
        if (StrUtil.isEmpty(strategyName)) {
            throw new RuntimeException("导入策略名称为空，导入失败");
        } else if (!IMPORT_EXCEL_STRATEGY_MAP.containsKey(strategyName)) {
            this.logger.warn("导入策略不存在；{}", strategyName);
            throw new RuntimeException("导入策略不存在：" + strategyName);
        } else {
            IExcelStrategyProcess strategySupport = IMPORT_EXCEL_STRATEGY_MAP.get(strategyName);
            if (ObjectUtil.isEmpty(strategySupport)) {
                this.logger.warn("策略未实例化；{}", strategyName);
                throw new RuntimeException("策略未实例化：" + strategyName);
            } else {
                return strategySupport;
            }
        }
    }
}