package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartling.marketo.sdk.HasToBeMappedToJson;
import com.smartling.marketo.sdk.domain.Asset;

public class Form extends Asset {
    private String Locale;
    private String Language;
    private KnownVisitor knownVisitor;
    private String buttonLabel;
    private String waitingLabel;

    public String getLocale() {
        return Locale;
    }

    public void setLocale(String locale) {
        this.Locale = locale;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language = language;
    }

    public KnownVisitor getKnownVisitor() {
        return knownVisitor;
    }

    public void setKnownVisitor(KnownVisitor knownVisitor) {
        this.knownVisitor = knownVisitor;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getWaitingLabel() {
        return waitingLabel;
    }

    public void setWaitingLabel(String waitingLabel) {
        this.waitingLabel = waitingLabel;
    }

    public static class KnownVisitor implements HasToBeMappedToJson {
        private KnownVisitorType type;
        private String template;

        public KnownVisitorType getType() {
            return type;
        }

        public void setType(KnownVisitorType type) {
            this.type = type;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }
    }

    public enum KnownVisitorType {
        FORM("form"), CUSTOM("custom");

        private String key;

        KnownVisitorType(String key) {
            this.key = key;
        }

        @JsonCreator
        public static KnownVisitorType fromString(String key) {
            return key == null
                    ? null
                    : KnownVisitorType.valueOf(key.toUpperCase());
        }

        @JsonValue
        public String getKey() {
            return key;
        }
    }
}
