package com.smartling.marketo.sdk.domain.form;

import java.util.List;

public class FormField
{
    private String id;
    private String label;
    private int rowNumber;
    private int columnNumber;
    private int maxLength;
    private String dataType;
    private String instructions;
    private String defaultValue;
    private String validationMessage;
    private VisibilityRules visibilityRules;
    private String hintText;
    private FieldMetaData fieldMetaData;
    private String text;
    private List<String> fields;

    private Boolean required;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public VisibilityRules getVisibilityRules() {
        return visibilityRules;
    }

    public void setVisibilityRules(VisibilityRules visibilityRules) {
        this.visibilityRules = visibilityRules;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    public void setFieldMetaData(FieldMetaData fieldMetaData) {
        this.fieldMetaData = fieldMetaData;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Boolean isRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "FormField{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", rowNumber=" + rowNumber +
                ", columnNumber=" + columnNumber +
                ", maxLength=" + maxLength +
                ", dataType='" + dataType + '\'' +
                ", instructions='" + instructions + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", validationMessage='" + validationMessage + '\'' +
                ", visibilityRules=" + visibilityRules +
                ", hintText='" + hintText + '\'' +
                ", fieldMetaData=" + fieldMetaData +
                ", text='" + text + '\'' +
                ", fields=" + fields +
                ", required=" + required +
                '}';
    }
}
