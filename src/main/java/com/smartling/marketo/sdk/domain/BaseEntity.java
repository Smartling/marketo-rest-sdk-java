package com.smartling.marketo.sdk.domain;

import com.smartling.marketo.sdk.domain.folder.Folder;

import java.util.Date;

public class BaseEntity {
    private int id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Folder folder = new Folder();
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "BaseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", folder=" + folder +
                ", url='" + url + '\'' +
                '}';
    }
}
