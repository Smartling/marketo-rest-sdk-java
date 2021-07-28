package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoProgramClient;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgramIntegrationTest extends BaseIntegrationTest {
    private static final String TEST_PROGRAM_NAME = "Program For Integration Tests";
    private static final FolderId TEST_FOLDER_ID = new FolderId(158, FolderType.FOLDER);
    private static final int TEST_ENGAGEMENT_PROGRAM_ID = 1051;
    private static final int TEST_PROGRAM_ID = 1047;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private MarketoProgramClient marketoProgramClient;

    @Before
    public void setUp() {
        marketoProgramClient = marketoClientManager.getMarketoProgramClient();
    }

    @Test
    public void shouldGetPrograms() throws Exception {
        List<Program> programs = marketoProgramClient.getPrograms(0, 1, null);

        assertThat(programs).hasSize(1);
        assertThat(programs.get(0).getId()).isPositive();
        assertThat(programs.get(0).getName()).isNotEmpty();
        assertThat(programs.get(0).getUpdatedAt()).isNotNull();
        assertThat(programs.get(0).getStatus()).isNotNull();
        assertThat(programs.get(0).getUrl()).isNotEmpty();
        assertThat(programs.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldGetProgramsWithFilter() throws Exception {
        List<Program> programs = marketoProgramClient.getPrograms(0, 1, TEST_FOLDER_ID);

        assertThat(programs).hasSize(1);
        assertThat(programs.get(0).getId()).isPositive();
        assertThat(programs.get(0).getName()).isNotEmpty();
        assertThat(programs.get(0).getUpdatedAt()).isNotNull();
        assertThat(programs.get(0).getStatus()).isNotNull();
        assertThat(programs.get(0).getUrl()).isNotEmpty();
        assertThat(programs.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<Program> programs = marketoProgramClient.getPrograms(10000, 1, null);

        assertThat(programs).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoProgramClient.getPrograms(-5, 5, null);
    }

    @Test
    public void shouldGetProgramById() throws Exception {
        Program program = marketoProgramClient.getProgramById(TEST_PROGRAM_ID);

        assertThat(program).isNotNull();
        assertThat(program.getId()).isEqualTo(TEST_PROGRAM_ID);
        assertThat(program.getName()).isNotEmpty();
        assertThat(program.getUrl()).isNotEmpty();
        assertThat(program.getFolder()).isNotNull();
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindProgramById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Program[id = 42] not found");

        marketoProgramClient.getProgramById(42);
    }

    @Test
    public void shouldGetProgramsByName() throws Exception {
        List<Program> programs = marketoProgramClient.getProgramsByName(TEST_PROGRAM_NAME);

        assertThat(programs).haveAtLeast(1, new EntityWithName(TEST_PROGRAM_NAME));
    }

    @Ignore
    @Test
    public void shouldCloneProgram() throws Exception {
        String newProgramName = "integration-test-clone-" + UUID.randomUUID().toString();

        Program clone = marketoProgramClient.cloneProgram(TEST_PROGRAM_ID, newProgramName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newProgramName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldCloneEngagementProgram() throws Exception {
        String newProgramName = "integration-test-clone-" + UUID.randomUUID().toString();

        Program clone = marketoProgramClient.cloneProgram(TEST_ENGAGEMENT_PROGRAM_ID, newProgramName, TEST_FOLDER_ID);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newProgramName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_FOLDER_ID.getId());
    }

    @Ignore
    @Test
    public void shouldCloneProgramViaShorthandMethod() throws Exception {
        String newProgramName = "integration-test-clone-" + UUID.randomUUID().toString();
        Program existingProgram = marketoProgramClient.getProgramById(TEST_PROGRAM_ID);

        Program clone = marketoProgramClient.cloneProgram(existingProgram, newProgramName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newProgramName);
        assertThat(clone.getFolder().getValue()).isEqualTo(existingProgram.getFolder().getValue());
    }
}
