package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;

import java.util.List;

public interface MarketoFolderClient {

    FolderDetails getFolderById(FolderId folderId) throws MarketoApiException;

    List<FolderDetails> getFolders(FolderId root, Integer offset, int maxDepth, Integer limit, String workspace) throws MarketoApiException;

    List<FolderContentItem> getFolderContents(FolderId folder, Integer  offset, Integer maxReturn) throws MarketoApiException;

    List<FolderDetails> getFolderByName(String name, FolderType type, FolderId root) throws MarketoApiException;

    FolderDetails createFolder(String name, FolderId parent, String description) throws MarketoApiException;
}
