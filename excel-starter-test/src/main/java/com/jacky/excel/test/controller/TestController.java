package com.jacky.excel.test.controller;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.EasyExcel;
import com.jacky.excel.core.domain.IExcelServiceFactory;
import com.jacky.excel.core.enums.ExcelImportTypeEnum;
import com.jacky.excel.test.modlel.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @Resource
    private IExcelServiceFactory factory;


    @PostMapping("/testImport")
    public String testImportExcel(MultipartFile file,String strategy) {
        try {
            return factory.doImportExcel(file, strategy, ExcelImportTypeEnum.FULL_IMPORT.getCode());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }




    /** 导出模板*/
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        String fileName = "学生";

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition",
                "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        EasyExcel.write(outputStream, Student.class)
                .autoCloseStream(Boolean.TRUE)
                .sheet("学生").doWrite(ListUtil.empty());
        ;

    }


}
