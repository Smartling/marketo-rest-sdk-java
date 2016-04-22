package com.smartling.marketo.sdk.domain.form;

import com.smartling.marketo.sdk.domain.Asset;

import java.util.Collections;
import java.util.List;

public class Form extends Asset {
    private String formTheme;
    private String formLocale;
    private String customCss;
    private Boolean progressiveProfiling;
    private String labelPosition;
    private String fontFamily;
    private String fontSize;
    private KnownVisitor knownVisitor;
    private SubmitButton submitButton;
    private List<ThankYouPage> thankYouList = Collections.emptyList();
    private String formLanguage;

    public String getFormTheme() {
        return formTheme;
    }

    public void setFormTheme(String formTheme) {
        this.formTheme = formTheme;
    }

    public String getFormLocale() {
        return formLocale;
    }

    public void setFormLocale(String formLocale) {
        this.formLocale = formLocale;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public Boolean getProgressiveProfiling() {
        return progressiveProfiling;
    }

    public void setProgressiveProfiling(Boolean progressiveProfiling) {
        this.progressiveProfiling = progressiveProfiling;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public KnownVisitor getKnownVisitor() {
        return knownVisitor;
    }

    public void setKnownVisitor(KnownVisitor knownVisitor) {
        this.knownVisitor = knownVisitor;
    }

    public SubmitButton getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(SubmitButton submitButton) {
        this.submitButton = submitButton;
    }

    public List<ThankYouPage> getThankYouList() {
        return thankYouList;
    }

    public void setThankYouList(List<ThankYouPage> thankYouList) {
        this.thankYouList = thankYouList;
    }

    public String getFormLanguage() {
        return formLanguage;
    }

    public void setFormLanguage(String formLanguage) {
        this.formLanguage = formLanguage;
    }

    static class SubmitButton {
        private String label;
        private String waitingLabel;
        private int buttonLocation;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getWaitingLabel() {
            return waitingLabel;
        }

        public void setWaitingLabel(String waitingLabel) {
            this.waitingLabel = waitingLabel;
        }

        public int getButtonLocation() {
            return buttonLocation;
        }

        public void setButtonLocation(int buttonLocation) {
            this.buttonLocation = buttonLocation;
        }
    }

    static class ThankYouPage {
        private String followupType;
        private String followupValue;
        private String subjectField;
        private String operator;
        private boolean isDefault;
        private List<String> values = Collections.emptyList();

        public String getFollowupType() {
            return followupType;
        }

        public void setFollowupType(String followupType) {
            this.followupType = followupType;
        }

        public String getFollowupValue() {
            return followupValue;
        }

        public void setFollowupValue(String followupValue) {
            this.followupValue = followupValue;
        }

        public String getSubjectField() {
            return subjectField;
        }

        public void setSubjectField(String subjectField) {
            this.subjectField = subjectField;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    static class KnownVisitor {
        private String type;
        private String template;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }
    }
}
