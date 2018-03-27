package com.smartling.marketo.sdk.domain.folder;

public class Folder {
    private int value;
    private FolderType type;
    private String folderName;

    public int getValue() {
        return value;
    }

    public FolderType getType() {
        return type;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setType(FolderType type) {
        this.type = type;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString()
    {
        return "Folder{" + "value=" + value + ", type=" + type + ", folderName='" + folderName + '\'' + '}';
    }
}
