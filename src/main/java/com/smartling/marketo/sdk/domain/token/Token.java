package com.smartling.marketo.sdk.domain.token;

public class Token {
    String name;
    Type type;
    String value;
    String computedUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComputedUrl() {
        return computedUrl;
    }

    public void setComputedUrl(String computedUrl) {
        this.computedUrl = computedUrl;
    }

    public static enum Type {
        SCRIPT_BLOCK, RICH_TEXT, TEXT, SCORE, NUMBER, DATE, ICALENDAR
    }

//    public static enum Type {
//        SCRIPT_BLOCK("script block"), RICH_TEXT("rich text"), TEXT("text");
//
//        private final String name;
//
//        Type(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public Type valueOf
//    }
}
