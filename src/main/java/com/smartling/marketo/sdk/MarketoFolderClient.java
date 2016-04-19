package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;

import java.util.List;

public interface MarketoFolderClient extends MarketoClient {

    List<FolderDetails> getFolders(FolderId root, int offset, int maxDepth, int limit, String workspace) throws MarketoApiException;

}
