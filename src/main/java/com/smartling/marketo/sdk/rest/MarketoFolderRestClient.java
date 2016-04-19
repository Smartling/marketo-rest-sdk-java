package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFolderClient;
import com.smartling.marketo.sdk.rest.command.folder.GetFoldersCommand;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoFolderRestClient implements MarketoFolderClient {

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoFolderRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<FolderDetails> getFolders(FolderId root, int offset, int maxDepth, int limit, String workspace) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new GetFoldersCommand(root, offset, maxDepth, limit, workspace));

        return folders != null ? folders : Collections.<FolderDetails>emptyList();
    }
}
