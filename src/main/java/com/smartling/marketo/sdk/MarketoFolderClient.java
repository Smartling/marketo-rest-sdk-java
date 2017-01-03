package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;

import java.util.List;

public interface MarketoFolderClient {

    List<FolderDetails> getFolders(FolderId root, int offset, int maxDepth, int limit, String workspace) throws MarketoApiException;

    List<FolderContentItem> getFolderContents(FolderId folder, Integer  offset, Integer maxReturn) throws MarketoApiException;

    List<FolderDetails> getFolderByName(String name, FolderType type, FolderId root) throws MarketoApiException;
}
