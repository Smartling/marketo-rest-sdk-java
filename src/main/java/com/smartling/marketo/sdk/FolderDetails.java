package com.smartling.marketo.sdk;

import java.util.Date;

public class FolderDetails {
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private String url;
    private FolderId folderId;
    private String folderType;
    private FolderId parent;
    private String path;
    private boolean isArchive;
    private boolean isSystem;
    private int accessZoneId;
    private String workspace;
    private int id;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public FolderId getFolderId() {
        return folderId;
    }

    public String getFolderType() {
        return folderType;
    }

    public FolderId getParent() {
        return parent;
    }

    public String getPath() {
        return path;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public int getAccessZoneId() {
        return accessZoneId;
    }

    public String getWorkspace() {
        return workspace;
    }

    public int getId() {
        return id;
    }
}
