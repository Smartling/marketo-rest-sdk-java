package com.smartling.marketo.sdk;

import java.util.Objects;

public class FolderId implements HasToBeMappedToJson {
    private int id;
    private FolderType type;

    public FolderId() {
    }

    public FolderId(int id, FolderType type) {
        this.id = id;
        this.type = type;
    }

    public FolderId(Folder folder) {
        this.id = folder.getValue();
        this.type = folder.getType();
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FolderId folderId = (FolderId) o;
        return Objects.equals(id, folderId.id) && Objects.equals(type, folderId.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
