package com.jacky.excel.core.enums;

import lombok.Getter;

@Getter
public enum ImportDataTypeEnum {

    GATHER("GATHER", "汇总"),
    ERROR("ERROR", "失败"),
    WARNING("WARNING", "警告"),
    INFO("INFO", "正常");

    private String code;
    private String desc;

    private ImportDataTypeEnum(String code, String desc) {
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