package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.folder.FolderContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFolderClient;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.folder.CreateFolder;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderById;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderByName;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderContents;
import com.smartling.marketo.sdk.rest.command.folder.GetFoldersCommand;

import java.util.Collections;
import java.util.List;

public class MarketoFolderRestClient implements MarketoFolderClient {

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoFolderRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public FolderDetails getFolderById(FolderId folderId) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new GetFolderById(folderId));

        return folders != null && !folders.isEmpty()? folders.get(0) : null;
    }

    @Override
    public List<FolderDetails> getFolders(FolderId root, Integer offset, int maxDepth, Integer limit, String workspace) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new GetFoldersCommand(root, offset, maxDepth, limit, workspace));

        return folders != null ? folders : Collections.emptyList();
    }

    @Override
    public List<FolderContentItem> getFolderContents(FolderId folder, Integer offset, Integer maxReturn) throws MarketoApiException {
        List<FolderContentItem> assets = httpCommandExecutor.execute(new GetFolderContents(folder, offset, maxReturn));

        return assets != null ? assets : Collections.emptyList();
    }

    @Override
    public List<FolderDetails> getFolderByName(String name, FolderType type, FolderId root) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new GetFolderByName(name, type, root));

        return folders != null ? folders : Collections.emptyList();
    }

    @Override
    public FolderDetails createFolder(String name, FolderId parent, String description) throws MarketoApiException {
        List<FolderDetails> folders = httpCommandExecutor.execute(new CreateFolder(name, parent, description));
        return folders != null && !folders.isEmpty()? folders.get(0) : null;
    }
}
