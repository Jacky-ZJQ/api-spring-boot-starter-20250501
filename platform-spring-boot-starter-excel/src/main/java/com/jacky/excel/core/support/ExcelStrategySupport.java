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


/**
 * 策略支持类，实现了策略的自动注册和获取
 * 这个类是策略模式在项目中的核心实现
 */
public class ExcelStrategySupport implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(ExcelStrategySupport.class);

    private ApplicationContext applicationContext;

    private static final ConcurrentHashMap<String, IExcelStrategyProcess> IMPORT_EXCEL_STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * 构造函数，初始化ExcelStrategySupport
     */
    public ExcelStrategySupport() {
    }

    /**
     * 注入上下文
     *
     * 这是 ApplicationContextAware 接口的必须实现方法
     * 获取并保存 Spring 容器的上下文（ApplicationContext）实例，为后续从 Spring 容器中获取策略类提供基础
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 加载、注册策略
     *
     * 容器初始化完成 ：当 Spring 容器初始化完成后，触发 ContextRefreshedEvent 事件
     * 加载策略 ：由于 ExcelStrategySupport 实现了 ApplicationListener<ContextRefreshedEvent> 接口，Spring 容器会调用 onApplicationEvent() 方法
     * 注册策略：在 onApplicationEvent() 方法中，使用之前保存的 applicationContext 获取所有带有 StrategyModel 注解的策略类，并将它们注册到 IMPORT_EXCEL_STRATEGY_MAP 中
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.logger.info("Spring容器启动完成，开始加载excel策略...");
            Map<String, Object> strategyModelMap = this.applicationContext.getBeansWithAnnotation(StrategyModel.class);
            for(Map.Entry<String, Object> entry : strategyModelMap.entrySet()) {
                StrategyModel strategyModel = AnnotationUtil.getAnnotation(entry.getValue().getClass(), StrategyModel.class);
                if (entry.getValue() instanceof IExcelStrategyProcess) {
                    IMPORT_EXCEL_STRATEGY_MAP.put(strategyModel.strategyName(), (IExcelStrategyProcess)entry.getValue());
                }
            }
            this.logger.info("Spring容器加载excel策略完成:{}", JSON.toJSON(IMPORT_EXCEL_STRATEGY_MAP));
        } catch (Exception e) {
            this.logger.error("Spring容器加载excel策略失败:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * step2. 提供根据策略名获取，策略实例的方法
     * @param strategyName 策略名称
     * @return 策略实例
     */
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