package com.jacky.excel.test.strategy;

import com.jacky.excel.core.annotion.StrategyModel;
import com.jacky.excel.core.domain.strategy.flow.AbstractBaseImportExcel;
import com.jacky.excel.core.support.DataImportLog;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@StrategyModel(strategyName = "student")
public class StudentExcelStrategy extends AbstractBaseImportExcel {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<?> doReadFileAndCheck(MultipartFile multipartFile, List<DataImportLog> list, String s) {
        logger.info("StudentExcelStrategy.doReadFileAndCheck 实现属于Student策略类的 读取文件和校验业务数据");
        return Lists.newArrayList();
    }

    @Override
    public void doImplementExcelImport(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList, String importType) {
        //TODO 逻辑处理
        logger.info("StudentExcelStrategy.doImplementExcelImport 实现属于Student的具体业务导入");
    }


    @Override
    public void allImportOrIncrementImport(List<?> list, List<DataImportLog> list1) {
        //TODO 逻辑处理
        logger.info("StudentExcelStrategy.doAllImportOrIncrementImport 实现Student的具体 增量/全量 导入 ");
    }

    @Override
    public void doCheck(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList) {
        super.doCheck(qualifiedExcelDataList, importLogList);
    }
}
