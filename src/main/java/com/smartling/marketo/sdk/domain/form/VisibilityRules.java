package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartling.marketo.sdk.HasToBeMappedToJson;

import java.util.Collections;
import java.util.List;

public class VisibilityRules implements HasToBeMappedToJson {
    private RuleType ruleType;
    private List<Rule> rules;

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static class Rule {
        private String subjectField;
        private String operator;
        private String altLabel;
        private List<String> values;

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

        public String getAltLabel() {
            return altLabel;
        }

        public void setAltLabel(String altLabel) {
            this.altLabel = altLabel;
        }

        public List<String> getValues() {
            return values != null ? values : Collections.EMPTY_LIST;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    public enum RuleType {
        ALWAYSSHOW("alwaysShow"),
        SHOW("show"),
        HIDE("HIDE");


        private String key;

        RuleType(String key) {
            this.key = key;
        }

        @JsonCreator
        public static RuleType fromString(String key) {
            return key == null
                    ? null
                    : RuleType.valueOf(key.toUpperCase());
        }

        @JsonValue
        public String getKey() {
            return key;
        }
    }
}
