package com.smartling.marketo.sdk.domain.form;

public class PickListDTO
{
    private boolean isDefault;
    private String label;
    private boolean selected;
    private String  value;

    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault(boolean aDefault)
    {
        isDefault = aDefault;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PickListDTO that = (PickListDTO) o;

        if (isDefault != that.isDefault)
            return false;
        if (selected != that.selected)
            return false;
        if (label != null ? !label.equals(that.label) : that.label != null)
            return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode()
    {
        int result = (isDefault ? 1 : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (selected ? 1 : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "PickListDTO{" + "isDefault=" + isDefault + ", label='" + label + '\'' + ", selected=" + selected + ", value='" + value
                + '\'' + '}';
    }
}
