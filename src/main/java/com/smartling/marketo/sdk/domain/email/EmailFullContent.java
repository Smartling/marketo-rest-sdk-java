package com.smartling.marketo.sdk.domain.email;

import com.smartling.marketo.sdk.domain.Asset.Status;

public class EmailFullContent
{
    private int id;
    private Status status;
    private String content;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "EmailFullContent{" + "id=" + id + ", status=" + status + ", content='" + content + '\'' + '}';
    }
}
