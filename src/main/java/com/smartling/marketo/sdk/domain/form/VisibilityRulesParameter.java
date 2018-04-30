package com.smartling.marketo.sdk.domain.form;

import com.smartling.marketo.sdk.HasToBeMappedToJson;

import java.util.List;
import java.util.stream.Collectors;

public class VisibilityRulesParameter implements HasToBeMappedToJson {
    private RuleType ruleType;
    private List<Rule> rules;

    public VisibilityRulesParameter(VisibilityRules visibilityRules)
    {
        this.ruleType = visibilityRules.getRuleType();
        this.rules = visibilityRules.getRules().stream().map(Rule::new).collect(Collectors.toList());
    }

    public VisibilityRulesParameter()
    {
    }

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
        private List<PickListDTO> pickListValues;

        public Rule(VisibilityRules.Rule rule)
        {
            this.altLabel = rule.getAltLabel();
            this.subjectField = rule.getSubjectField();
            this.operator = rule.getOperator();
            this.values = rule.getValues();
            this.pickListValues = rule.getPicklistFilterValues();
        }

        public Rule()
        {
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

        public List<PickListDTO> getPickListValues()
        {
            return pickListValues;
        }

        public void setPickListValues(List<PickListDTO> pickListValues)
        {
            this.pickListValues = pickListValues;
        }

        @Override
        public String toString()
        {
            return "Rule{" + "subjectField='" + subjectField + '\'' + ", operator='" + operator + '\'' + ", altLabel='" + altLabel + '\''
                    + ", values=" + values + ", pickListValues=" + pickListValues + '}';
        }
    }
}
