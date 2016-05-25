package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderDetails;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.MarketoFolderClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class FolderIntegrationTest extends BaseIntegrationTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoFolderClient marketoFolderClient;

    @Before
    public void setUp() {
        marketoFolderClient = marketoClientManager.getMarketoFolderClient();
    }

    @Test
    public void shouldGetFolders() throws Exception {
        List<FolderDetails> folders = marketoFolderClient.getFolders(new FolderId(1, FolderType.FOLDER), 0, 1, 1, null);

        assertThat(folders).hasSize(1);
        assertThat(folders.get(0).getId()).isPositive();
        assertThat(folders.get(0).getName()).isNotEmpty();
        assertThat(folders.get(0).getDescription()).isNotEmpty();
        assertThat(folders.get(0).getCreatedAt()).isBefore(new Date());
        assertThat(folders.get(0).getUpdatedAt()).isBefore(new Date());
        assertThat(folders.get(0).getFolderId()).isNotNull();
        assertThat(folders.get(0).getPath()).isNotEmpty();
        assertThat(folders.get(0).getFolderId()).isNotNull();
        assertThat(folders.get(0).getFolderType()).isNotEmpty();
        assertThat(folders.get(0).getParent()).isNull();
        assertThat(folders.get(0).getWorkspace()).isNotEmpty();
    }
}
