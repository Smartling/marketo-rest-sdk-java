package com.smartling.marketo.sdk;

import com.google.common.collect.Lists;

import java.util.List;

public class EmailContentItem {
    private String htmlId;
    private String contentType;
    private List<Value> value = Lists.newArrayList();

    public String getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
