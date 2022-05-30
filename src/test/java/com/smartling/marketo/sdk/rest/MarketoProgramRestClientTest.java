package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.rest.command.program.CloneProgram;
import com.smartling.marketo.sdk.rest.command.program.GetProgramById;
import com.smartling.marketo.sdk.rest.command.program.GetPrograms;
import com.smartling.marketo.sdk.rest.command.program.GetProgramsByName;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class MarketoProgramRestClientTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoProgramRestClient testedInstance;

    @Test
    public void shouldRequestAllPrograms() throws Exception {
        Program program = new Program();
        given(executor.execute(isA(GetPrograms.class))).willReturn(Collections.singletonList(program));

        List<Program> programs = testedInstance.getPrograms(0, 10, null);

        assertThat(programs).contains(program);
    }

    @Test
    public void shouldRequestProgramListWithFolderFilter() throws Exception {
        Program program = new Program();
        given(executor.execute(isA(GetPrograms.class))).willReturn(Collections.singletonList(program));

        List<Program> programs = testedInstance.getPrograms(0, 10, new FolderId(1, FolderType.FOLDER));

        assertThat(programs).contains(program);
    }

    @Test
    public void shouldRequestProgramListWithUpdatedAtFilter() throws Exception {
        Program program = new Program();
        given(executor.execute(isA(GetPrograms.class))).willReturn(Collections.singletonList(program));

        List<Program> programs = testedInstance.getPrograms(0, 10, null, new Date(), new Date());

        assertThat(programs).contains(program);
    }

    @Test
    public void shouldReturnEmptyProgramListOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Program> programs = testedInstance.getPrograms(0, 10, new FolderId(1, FolderType.FOLDER));

        assertThat(programs).isEmpty();
    }

    @Test
    public void shouldReturnEmptyProgramListByNameOnNullResult() throws Exception {
        given(executor.execute(any(Command.class))).willReturn(null);

        List<Program> programs = testedInstance.getProgramsByName("name");

        assertThat(programs).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldRethrowApiErrorsDifferentFromPaginationIssues() throws Exception {
        given(executor.execute(any(Command.class))).willThrow(new MarketoApiException("100", ""));

        testedInstance.getPrograms(0, 10, new FolderId(1, FolderType.FOLDER));
    }

    @Test
    public void shouldGetProgramById() throws Exception {
        Program program = new Program();
        given(executor.execute(isA(GetProgramById.class))).willReturn(Collections.singletonList(program));

        Program result = testedInstance.getProgramById(42);

        assertThat(result).isEqualTo(program);
    }

    @Test
    public void shouldThrowMarketoApiExceptionIfProgramNotFound() throws Exception
    {
        int nonExistingId = 42;

        given(executor.execute(isA(GetProgramById.class))).willReturn(null);

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Program[id = 42] not found");

        testedInstance.getProgramById(nonExistingId);
    }

    @Test
    public void shouldGetProgramsByName() throws Exception {
        Program expected = new Program();
        given(executor.execute(isA(GetProgramsByName.class))).willReturn(Collections.singletonList(expected));

        List<Program> result = testedInstance.getProgramsByName("name");

        assertThat(result.get(0)).isEqualTo(expected);
    }

    @Test
    public void shouldGetPaginatedProgramsByName() throws Exception {
        Program expected = new Program();
        given(executor.execute(isA(GetProgramsByName.class))).willReturn(Collections.singletonList(expected));

        List<Program> result = testedInstance.getProgramsByName(0, 10, "name");

        assertThat(result.get(0)).isEqualTo(expected);
    }

    @Test
    public void shouldCloneProgram() throws Exception {
        Program clone = new Program();
        given(executor.execute(isA(CloneProgram.class))).willReturn(Collections.singletonList(clone));

        Program result = testedInstance.cloneProgram(42, "blah", new FolderId(999, FolderType.FOLDER));

        assertThat(result).isSameAs(clone);
    }

    @Test
    public void shouldCloneExistingProgram() throws Exception {
        Program clone = new Program();
        given(executor.execute(isA(CloneProgram.class))).willReturn(Collections.singletonList(clone));

        Program result = testedInstance.cloneProgram(new Program(), "blah");

        assertThat(result).isSameAs(clone);
    }
}