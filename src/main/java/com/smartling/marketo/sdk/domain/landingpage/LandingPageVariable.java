package com.smartling.marketo.sdk.domain.landingpage;

public class LandingPageVariable
{
    private String id;
    private String type;
    private String value;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
    public String toString()
    {
        return "LandingPageVariable{" + "id='" + id + '\'' + ", type='" + type + '\'' + ", value='" + value + '\'' + '}';
    }
}
