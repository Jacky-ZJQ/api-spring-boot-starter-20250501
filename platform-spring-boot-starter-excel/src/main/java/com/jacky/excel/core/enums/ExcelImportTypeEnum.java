package com.jacky.excel.core.enums;

import lombok.Getter;



@Getter
public enum ExcelImportTypeEnum {

    FULL_IMPORT("FULL_IMPORT", "全量导入"),
    INCREMENTAL_IMPORT("INCREMENTAL_IMPORT", "增量导入");

    private String code;
    private String desc;

    ExcelImportTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}