package com.jacky.excel.core.domain.process.impl;

import cn.hutool.core.util.EnumUtil;
import com.jacky.excel.core.domain.process.AbstractImportExcel;
import com.jacky.excel.core.domain.strategy.IExcelStrategyProcess;
import com.jacky.excel.core.enums.ExcelImportTypeEnum;
import com.jacky.excel.core.enums.ImportDataTypeEnum;
import com.jacky.excel.core.support.DataImportLog;

import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ExcelImpl extends AbstractImportExcel {

    @Override
    protected String importLogInsertBuildResult(String importType, IExcelStrategyProcess excelStrategyProcess, List<DataImportLog> importLogList, String strategyName) {
        if (importLogList.isEmpty()) {
            return "导入成功，无日志信息";
        } else {
            LinkedHashMap<String, ExcelImportTypeEnum> excelImportEnumMap = EnumUtil.getEnumMap(ExcelImportTypeEnum.class);
            DataImportLog parentDataImportInfo = importLogList.stream().filter((k) -> ImportDataTypeEnum.GATHER.getCode().equals(k.getInfoType())).findFirst().orElse(null);

            assert parentDataImportInfo != null;

            parentDataImportInfo.setImportProgram(excelImportEnumMap.get(importType).getDesc());
            parentDataImportInfo.setCreateTime(new Date());
            parentDataImportInfo.setTableName(strategyName);
            long Count = importLogList.stream().filter((k) -> ImportDataTypeEnum.GATHER.getCode().equals(k.getInfoType())).count();
            long errorCount = importLogList.stream().filter((k) -> ImportDataTypeEnum.ERROR.getCode().equals(k.getInfoType())).count();
            long warningCount = importLogList.stream().filter((k) -> ImportDataTypeEnum.WARNING.getCode().equals(k.getInfoType())).count();
            return MessageFormat.format("导入数据完成，总数据：{0}条；错误信息：{1}条；告警信息：{2}条；详情信息请查看日志明细", Count, errorCount, warningCount);
        }
    }
}
