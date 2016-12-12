package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoProgramClient;
import com.smartling.marketo.sdk.domain.program.Program;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.rest.command.program.CloneProgram;
import com.smartling.marketo.sdk.rest.command.program.GetProgramById;
import com.smartling.marketo.sdk.rest.command.program.GetPrograms;
import com.smartling.marketo.sdk.rest.command.program.GetProgramsByName;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoProgramRestClient implements MarketoProgramClient {

    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoProgramRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Program> getPrograms(int offset, int limit, FolderId folder) throws MarketoApiException {
        List<Program> programs = httpCommandExecutor.execute(new GetPrograms(offset, limit, folder));
        return (programs != null) ? programs : Collections.emptyList();
    }

    @Override
    public Program getProgramById(int id) throws MarketoApiException {
        List<Program> execute = httpCommandExecutor.execute(new GetProgramById(id));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("Program[id = %d] not found", id));
        }
    }

    @Override
    public List<Program> getProgramsByName(String name) throws MarketoApiException {
        List<Program> programs = httpCommandExecutor.execute(new GetProgramsByName(name));
        return programs != null ? programs : Collections.emptyList();
    }

    @Override
    public Program cloneProgram(int sourceProgramId, String newProgramName, FolderId folderId) throws MarketoApiException {
        List<Program> cloned = httpCommandExecutor.execute(new CloneProgram(sourceProgramId, newProgramName, folderId));
        return cloned.get(0);
    }

    @Override
    public Program cloneProgram(Program existingProgram, String newProgramName) throws MarketoApiException {
        return cloneProgram(existingProgram.getId(), newProgramName, new FolderId(existingProgram.getFolder()));
    }
}
