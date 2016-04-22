package com.smartling.marketo.sdk.domain.form;

import java.util.List;

public class FormField {
    private String id;
    private VisibilityRule visibilityRules;
    private String label;
    private String dataType;
    private Integer labelWidth;
    private Integer fieldWidth;
    private String validationMessage;
    private String instructions;
    private Boolean required;
    private Integer rowNumber;
    private Integer index;
    private Boolean formPrefill;
    private String hintText;

    public VisibilityRule getVisibilityRules() {
        return visibilityRules;
    }

    public void setVisibilityRules(VisibilityRule visibilityRules) {
        this.visibilityRules = visibilityRules;
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

    public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Integer getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(Integer fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getFormPrefill() {
        return formPrefill;
    }

    public void setFormPrefill(Boolean formPrefill) {
        this.formPrefill = formPrefill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    static class VisibilityRule {
        private String ruleType;
        private List<Rule> rules;

        public String getRuleType() {
            return ruleType;
        }

        public void setRuleType(String ruleType) {
            this.ruleType = ruleType;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }
    }

    static class Rule {
        private String subjectField;
        private String operator;
        private String altLabel;

        public String getSubjectField() {
            return subjectField;
        }

        public void setSubjectField(String subjectField) {
            this.subjectField = subjectField;
        }
    }
}
