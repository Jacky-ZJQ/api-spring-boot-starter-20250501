package com.jacky.excel.core.domain.process;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import com.jacky.excel.core.domain.IExcelServiceFactory;
import com.jacky.excel.core.domain.strategy.IExcelStrategyProcess;
import com.jacky.excel.core.enums.ExcelImportTypeEnum;
import com.jacky.excel.core.support.DataImportLog;
import com.jacky.excel.core.support.ExcelStrategySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Jacky.Z
 * @date: 2025/5/1 15:33
 * @description：
 */
public abstract class AbstractImportExcel extends ExcelStrategySupport implements IExcelServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractImportExcel.class);
    private IExcelStrategyProcess excelStrategyProcess;

    public String doImportExcel(MultipartFile file, String strategyName, String importType) {
        logger.info("开始导入Excel文件，文件名：{}，策略：{}，导入类型：{}", file.getOriginalFilename(), strategyName, importType);
        TimeInterval timer = new TimeInterval();
        List<DataImportLog> importLogList = new ArrayList<>();

        this.checkFile(file);
        logger.info("step 1: 文件格式校验完成，耗时：{} S", timer.intervalSecond("1"));
        this.excelStrategyProcess = super.getImportStrategy(strategyName);

        logger.info("step 2: 获取导入策略对象，耗时：{} S", timer.intervalSecond("2"));
        List<?> qualifiedDataList = this.doReadFileAndCheck(file, importLogList, importType);

        logger.info("step 3: 读取excel数据，耗时：{} S", timer.intervalSecond("3"));
        this.implementExcelImport(qualifiedDataList, importType, importLogList);

        logger.info("step 4: 逻辑业务执行导入，耗时：{} S", timer.intervalSecond("4"));
        this.allImportOrIncrementImport(qualifiedDataList, importType, importLogList);

        logger.info("step 5: 全量导入 or 增量导入  业务处理，耗时：{} S", timer.intervalSecond("5"));
        return this.importLogInsertBuildResult(importType, this.excelStrategyProcess, importLogList, strategyName);
    }


    /**
     * 构建导入日志信息msg
     *
     * @param importType      导入模式
     * @param strategyProcess 导入策略
     * @param importLogList   日志集合
     * @param strategyName    策略名称（表名）
     * @return res
     */
    protected abstract String importLogInsertBuildResult(String importType, IExcelStrategyProcess strategyProcess, List<DataImportLog> importLogList, String strategyName);


    /**
     * 5.全量导入 or 增量导入 业务处理
     * <p>
     * 全量导入（内部存在外部不存在，内部删除）
     * </p>
     *
     * @param qualifiedExcelDataList excel数据
     * @param importType             导入模式
     * @param dataImportLogList          日志集合
     */
    private void allImportOrIncrementImport(List<?> qualifiedExcelDataList, String importType, List<DataImportLog> dataImportLogList) {
        if (importType.equals(ExcelImportTypeEnum.INCREMENTAL_IMPORT.getCode())) {
            logger.info("非增量导入不处理删除数据");
        } else if (CollectionUtils.isEmpty(qualifiedExcelDataList)) {
            logger.info("合法数据为空，增量导入终止");
        } else {
            this.excelStrategyProcess.allImportOrIncrementImport(qualifiedExcelDataList, dataImportLogList);
        }
    }

    /**
     * 逻辑业务执行策略导入
     *
     * <p>
     * 增量导入
     * 1.外部存在，内部不存在，新增
     * 2.外部存在，内部也存在，修改
     * <p>
     * 全量导入
     * 1.外部存在，内部不存在，做新增
     * 2.外部存在，内部也存在，做修改
     * 3.外部不存在，内部存在，内部做删除
     *
     * @param qualifiedExcelDataList excel数据
     * @param importType             导入类型
     * @param dataImportLogList          日志集
     */
    private void implementExcelImport(List<?> qualifiedExcelDataList, String importType, List<DataImportLog> dataImportLogList) {
        if (CollectionUtils.isEmpty(qualifiedExcelDataList)) {
            logger.info("合法数据为空，逻辑业务执行策略导入终止");
        } else {
            this.excelStrategyProcess.doImplementExcelImport(qualifiedExcelDataList, dataImportLogList, importType);
        }
    }

    /**
     * 读取excel文件并执行自定义校验规则
     *
     * @param file          文件
     * @param dataImportLogList 日志集
     * @param importType    导入类型
     * @return res
     */
    private List<?> doReadFileAndCheck(MultipartFile file, List<DataImportLog> dataImportLogList, String importType) {
        return this.excelStrategyProcess.doReadFileAndCheck(file, dataImportLogList, importType);
    }

    /**
     * 校验excel文件格式
     *
     * @param file file
     */
    private void checkFile(MultipartFile file) {
        String[] fileSuffix = new String[]{"xls", "xlsx", "CSV"};
        if (null == file) {
            throw new RuntimeException("请选择excel文件!");
        } else {
            String fileName = file.getOriginalFilename();
            if (StrUtil.isBlank(fileName)) {
                throw new RuntimeException("获取文件名称失败，请求选择正确的文件!");
            } else {
                if (Arrays.stream(fileSuffix).noneMatch(fileName::endsWith)) {
                    throw new RuntimeException("文件类型错误，请求选择excel文件");
                }
            }
        }
    }

}
