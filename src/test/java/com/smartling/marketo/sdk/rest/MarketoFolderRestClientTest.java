package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.folder.FolderContentItem;
import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderById;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderByName;
import com.smartling.marketo.sdk.rest.command.folder.GetFolderContents;
import com.smartling.marketo.sdk.rest.command.folder.GetFoldersCommand;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoFolderRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoFolderRestClient testedInstance;

    @Test
    public void shouldReturnFolderById() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFolderById.class))).willReturn(Collections.singletonList(folder));

        FolderDetails folderResult = testedInstance.getFolderById(new FolderId(1, FolderType.FOLDER));

        assertEquals(folderResult, folder);
    }

    @Test
    public void shouldReturnFolderWithFilter() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFoldersCommand.class))).willReturn(Collections.singletonList(folder));

        List<FolderDetails> folders = testedInstance.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 10, null);

        assertThat(folders).contains(folder);
    }

    @Test
    public void shouldReturnFolderContents() throws Exception {
        FolderContentItem asset = new FolderContentItem();
        given(executor.execute(isA(GetFolderContents.class))).willReturn(Collections.singletonList(asset));

        List<FolderContentItem> assets = testedInstance.getFolderContents(new FolderId(1, FolderType.FOLDER), 0, 1);

        assertThat(assets).contains(asset);
    }

    @Test
    public void shouldReturnFolderByName() throws Exception {
        FolderDetails folder = new FolderDetails();
        given(executor.execute(isA(GetFolderByName.class))).willReturn(Collections.singletonList(folder));

        List<FolderDetails> folders = testedInstance.getFolderByName("Dummy folder", FolderType.FOLDER, new FolderId(1, FolderType.FOLDER));

        assertThat(folders).contains(folder);
    }

}