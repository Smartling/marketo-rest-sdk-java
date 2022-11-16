package com.smartling.marketo.sdk.domain.form;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectItemValue extends Value
{
    boolean isDefault;
    boolean selected;

    @JsonProperty("isDefault")
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @JsonProperty("selected")
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString()
    {
        return "Value{" + "label='" + this.getLabel() + '\'' + ", value='" + this.getValue() + '\'' +
                ", isDefault=" + isDefault + ", selected=" + selected + '}';
    }
}
