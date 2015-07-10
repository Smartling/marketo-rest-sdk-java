package com.smartling.marketo.sdk;

public class FolderId implements JsonParameter {
    private int id;
    private FolderType type;

    public FolderId() {

    }

    public FolderId(int id, FolderType type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FolderType getType() {
        return type;
    }

    public void setType(FolderType type) {
        this.type = type;
    }
}
