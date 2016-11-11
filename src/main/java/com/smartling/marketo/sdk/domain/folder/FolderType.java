package com.smartling.marketo.sdk.domain.folder;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FolderType {
    @JsonProperty("Folder")
    FOLDER,
    @JsonProperty("Program")
    PROGRAM
}
