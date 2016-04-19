package com.smartling.marketo.sdk.domain.email;

import com.google.common.collect.Lists;

import java.util.List;

public class EmailTextContentItem extends EmailContentItem {
    private List<Value> value = Lists.newArrayList();

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
