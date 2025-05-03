package com.jacky.excel.test.modlel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(value = 40)
public class Student {

    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("年龄")
    @ColumnWidth(20)
    private Integer age;
}
