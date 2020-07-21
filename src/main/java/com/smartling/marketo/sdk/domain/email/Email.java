package com.smartling.marketo.sdk.domain.email;

import com.smartling.marketo.sdk.domain.Asset;

import java.util.Objects;

public class Email extends Asset {
    private TextField subject = new TextField();
    private TextField fromName = new TextField();
    private Integer template;
    private int version;
    private String preHeader;
    private boolean textOnly;
    private boolean autoCopyToText;

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

    public String getPreHeader()
    {
        return preHeader;
    }

    public void setPreHeader(String preHeader)
    {
        this.preHeader = preHeader;
    }

    public boolean isTextOnly()
    {
        return textOnly;
    }

    public void setTextOnly(boolean textOnly)
    {
        this.textOnly = textOnly;
    }

    public boolean isAutoCopyToText()
    {
        return autoCopyToText;
    }

    public void setAutoCopyToText(boolean autoCopyToText)
    {
        this.autoCopyToText = autoCopyToText;
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

    @Override
    public String toString()
    {
        return "Email{" +
                "subject=" + subject +
                ", fromName=" + fromName +
                ", template=" + template +
                ", version=" + version +
                ", preHeader='" + preHeader + '\'' +
                ", textOnly=" + textOnly +
                ", autoCopyToText=" + autoCopyToText +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email = (Email) o;
        return version == email.version &&
                textOnly == email.textOnly &&
                autoCopyToText == email.autoCopyToText &&
                Objects.equals(subject, email.subject) &&
                Objects.equals(fromName, email.fromName) &&
                Objects.equals(template, email.template) &&
                Objects.equals(preHeader, email.preHeader);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(subject, fromName, template, version, preHeader, textOnly, autoCopyToText);
    }
}
