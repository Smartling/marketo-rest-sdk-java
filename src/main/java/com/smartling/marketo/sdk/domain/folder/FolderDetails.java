package com.smartling.marketo.sdk.domain.folder;

import java.util.Date;

public class FolderDetails {

    private static final String EMAIL_FOLDER_TYPE = "Email";
    private static final String FORM_FOLDER_TYPE = "Landing Page Form";
    private static final String LANDING_PAGE_FOLDER_TYPE = "Landing Page";
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FolderId getFolderId() {
        return folderId;
    }

    public void setFolderId(FolderId folderId) {
        this.folderId = folderId;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public FolderId getParent() {
        return parent;
    }

    public void setParent(FolderId parent) {
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public int getAccessZoneId() {
        return accessZoneId;
    }

    public void setAccessZoneId(int accessZoneId) {
        this.accessZoneId = accessZoneId;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFolderForEmails() {
        return this.folderType.equals(EMAIL_FOLDER_TYPE) ||
                this.folderType.equals(EVENT_FOLDER_TYPE) ||
                this.folderType.equals(PROGRAM_FOLDER_TYPE) ||
                (this.folderType.equals(MARKETING_FOLDER_TYPE) && this.parent.getType().equals(FolderType.PROGRAM));
    }

    public boolean isFolderForForms() {
        return this.folderType.equals(FORM_FOLDER_TYPE) ||
                this.folderType.equals(EVENT_FOLDER_TYPE) ||
                this.folderType.equals(PROGRAM_FOLDER_TYPE) ||
                (this.folderType.equals(MARKETING_FOLDER_TYPE) && this.parent.getType().equals(FolderType.PROGRAM));
    }

    public boolean isFolderForLandingPages() {
        return this.folderType.equals(LANDING_PAGE_FOLDER_TYPE) ||
                this.folderType.equals(EVENT_FOLDER_TYPE) ||
                this.folderType.equals(PROGRAM_FOLDER_TYPE) ||
                (this.folderType.equals(MARKETING_FOLDER_TYPE) && this.parent.getType().equals(FolderType.PROGRAM));
    }
}
