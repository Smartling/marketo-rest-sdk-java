package com.smartling.marketo.sdk.domain.form;

public class FormField {
    private String id;
    private String label;
    private String dataType;
    private String instructions;
    private String defaultValue;
    private String validationMessage;
    private VisibilityRules visibilityRules;
    private String hintText;

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

}
