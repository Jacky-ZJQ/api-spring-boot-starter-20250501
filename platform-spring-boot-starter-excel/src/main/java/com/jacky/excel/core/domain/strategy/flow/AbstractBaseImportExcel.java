package com.jacky.excel.core.domain.strategy.flow;

import com.jacky.excel.core.domain.strategy.IExcelStrategyProcess;
import com.jacky.excel.core.support.DataImportLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: Jacky.Z
 * @date: 2025/5/3 15:33
 * @description：
 */
public abstract class AbstractBaseImportExcel implements IExcelStrategyProcess {

    private final Logger logger = LoggerFactory.getLogger(AbstractBaseImportExcel.class);

    public void doCheck(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList) {
        if (importLogList.isEmpty()) {
            this.logger.error("校验数据为空");
        } else {
            for(Object object : qualifiedExcelDataList) {
                Field[] declaredFields = object.getClass().getDeclaredFields();
                for(Field field : declaredFields) {
                    logger.info("field:{}", field.getName());
                    // TODO 针对每个字段完成自定义校验规则，并创建导入信息
                }
            }

        }
    }
}
