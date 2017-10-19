package com.smartling.marketo.sdk.domain.email;

public class EmailVariable
{
    private String id;
    private String value;
    private boolean moduleScope;
    private String moduleId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public boolean isModuleScope()
    {
        return moduleScope;
    }

    public void setModuleScope(boolean moduleScope)
    {
        this.moduleScope = moduleScope;
    }

    public String getModuleId()
    {
        return moduleId;
    }

    public void setModuleId(String moduleId)
    {
        this.moduleId = moduleId;
    }
}
