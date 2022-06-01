package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.program.Program;

import java.util.Date;
import java.util.List;

public interface MarketoProgramClient {
    List<Program> getPrograms(Integer offset, Integer limit, FolderId folder) throws MarketoApiException;

    List<Program> getPrograms(Integer offset, Integer limit, FolderId folder, Date earliestUpdatedAt, Date latestUpdatedAt) throws MarketoApiException;

    Program getProgramById(int id) throws MarketoApiException;

    List<Program> getProgramsByName(String name) throws MarketoApiException;

    List<Program> getProgramsByName(Integer offset, Integer limit, String name) throws MarketoApiException;

    Program cloneProgram(int sourceProgramId, String name, FolderId folder) throws MarketoApiException;

    Program cloneProgram(Program existingProgram, String newProgramName) throws MarketoApiException;

    Program createProgram(Program newProgram) throws MarketoApiException;
}
