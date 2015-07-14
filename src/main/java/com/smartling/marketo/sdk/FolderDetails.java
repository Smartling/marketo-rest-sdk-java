package com.smartling.marketo.sdk;

import java.util.Date;

public class FolderDetails {

    private static final String EMAIL_FOLDER_TYPE = "Email";
    private static final String PROGRAM_FOLDER_TYPE = "Email Batch Program";
    private static final String EVENT_FOLDER_TYPE = "Marketing Event";
    private static final String MARKETING_FOLDER_TYPE = "Marketing Folder";

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

    public boolean isFolderForEmails() {
        return this.folderType.equals(EMAIL_FOLDER_TYPE) ||
                this.folderType.equals(EVENT_FOLDER_TYPE) ||
                this.folderType.equals(PROGRAM_FOLDER_TYPE) ||
                (this.folderType.equals(MARKETING_FOLDER_TYPE) && this.parent.getType().equals(FolderType.PROGRAM));
    }
}
