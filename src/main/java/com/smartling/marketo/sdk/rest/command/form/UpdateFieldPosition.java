package com.smartling.marketo.sdk.rest.command.form;

import java.util.List;

public class UpdateFieldPosition {
    private Integer columnNumber;
    private Integer rowNumber;
    private List<UpdateFieldPosition> fieldList;
    private String fieldName;

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<UpdateFieldPosition> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<UpdateFieldPosition> fieldList) {
        this.fieldList = fieldList;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "UpdateFieldPosition{" +
                "columnNumber=" + columnNumber +
                ", rowNumber=" + rowNumber +
                ", fieldList=" + fieldList +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
