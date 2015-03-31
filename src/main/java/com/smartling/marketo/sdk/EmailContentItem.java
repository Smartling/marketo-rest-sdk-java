package com.smartling.marketo.sdk;

import java.util.Collections;
import java.util.List;

public class EmailContentItem {
    private String htmlId;
    private String htmlContent;
    private String textContent;
    private List<Value> value = Collections.emptyList();

    public String getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    public static final class Value {
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
