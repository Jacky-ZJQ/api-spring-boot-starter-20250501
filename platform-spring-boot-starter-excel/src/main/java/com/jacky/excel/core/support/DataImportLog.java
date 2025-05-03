package com.jacky.excel.core.support;

import lombok.Data;

import java.util.Date;

@Data
public class DataImportLog {
    private Integer id;
    private String tableName;
    private String infoType;
    private String remark;
    private String creator;
    private Date createTime;
    private String importProgram;
    private String uid;
    private String errorDetails;
}