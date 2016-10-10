package com.smartling.marketo.sdk.domain.email;

import com.smartling.marketo.sdk.domain.Asset;

public class Email extends Asset {
    private TextField subject = new TextField();
    private TextField fromName = new TextField();
    private Integer template;
    private int version;

    public String getSubject() {
        return subject.getValue();
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public String getFromName() {
        return fromName.getValue();
    }

    public void setFromName(String value) {
        fromName.setValue(value);
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class TextField {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
