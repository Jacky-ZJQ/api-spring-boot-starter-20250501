package com.jacky.excel.core.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Jacky.Z
 * @date: 2025/5/3 15:33
 * @description：抽象工厂类
 */
public interface IExcelServiceFactory {
    /**
     * 导入
     *
     * @param file         文件流
     * @param strategyName 策略名称
     * @param importType   导入类型（增量/全量）
     * @return res
     * @throws Exception 导入结果msg
     */
    String doImportExcel(MultipartFile file, String strategyName, String importType);
}