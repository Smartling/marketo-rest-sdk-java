package com.smartling.marketo.sdk.domain.form;

import com.smartling.marketo.sdk.HasToBeMappedToJson;

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

    @Override
    public String toString()
    {
        return "VisibilityRules{" + "ruleType=" + ruleType + ", rules=" + rules + '}';
    }

    public static class Rule {
        private String subjectField;
        private String operator;
        private String altLabel;
        private List<String> values;
        private List<PickListDTO> picklistFilterValues;

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
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        public List<PickListDTO> getPicklistFilterValues()
        {
            return picklistFilterValues;
        }

        public void setPicklistFilterValues(List<PickListDTO> picklistFilterValues)
        {
            this.picklistFilterValues = picklistFilterValues;
        }

        @Override
        public String toString()
        {
            return "Rule{" + "subjectField='" + subjectField + '\'' + ", operator='" + operator + '\'' + ", altLabel='" + altLabel + '\''
                    + ", values=" + values + ", picklistFilterValues=" + picklistFilterValues + '}';
        }
    }
}
