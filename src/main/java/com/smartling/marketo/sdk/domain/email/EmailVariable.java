package com.smartling.marketo.sdk.domain.email;

public class EmailVariable
{
    private String name;
    private String value;
    private boolean moduleScope;
    private String moduleId;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    @Override
    public String toString()
    {
        return "EmailVariable{" + "name='" + name + '\'' + ", value='" + value + '\'' + ", moduleScope=" + moduleScope + ", moduleId='"
                + moduleId + '\'' + '}';
    }
}
