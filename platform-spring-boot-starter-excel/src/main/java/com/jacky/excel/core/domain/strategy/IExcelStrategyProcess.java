package com.jacky.excel.core.domain.strategy;

import com.jacky.excel.core.support.DataImportLog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author: Jacky.Z
 * @date: 2025/5/1 15:33
 * @description: 策略模式， IExcelStrategyProcess 定义了Excel导入流程的各个具体处理步骤
 */
public interface IExcelStrategyProcess {

    /**
     * 读取文件并校验
     *
     * @param file          file
     * @param importLogList req
     * @param importType    req
     * @return res
     * @throws IOException IO异常
     */
    List<?> doReadFileAndCheck(MultipartFile file, List<DataImportLog> importLogList, String importType) ;

    /**
     * 实现业务导入
     *
     * @param qualifiedExcelDataList req
     * @param importLogList          req
     * @param importType             req
     */
    void doImplementExcelImport(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList, String importType);

    /**
     * 增量/全量 导入
     *
     * @param qualifiedExcelDataList req
     * @param importLogList          req
     */
    void allImportOrIncrementImport(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList);

    /**
     * 校验
     *
     * @param qualifiedExcelDataList req
     * @param importLogList          req
     */
    void doCheck(List<?> qualifiedExcelDataList, List<DataImportLog> importLogList);

}
