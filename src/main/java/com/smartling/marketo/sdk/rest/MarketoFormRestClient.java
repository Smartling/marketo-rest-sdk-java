package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.rest.command.form.CloneFormCommand;
import com.smartling.marketo.sdk.rest.command.form.GetFormsByNameCommand;
import com.smartling.marketo.sdk.rest.command.form.GetFormsCommand;
import com.smartling.marketo.sdk.rest.command.form.LoadFormByIdCommand;
import com.smartling.marketo.sdk.rest.command.form.LoadFormFields;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormCommand;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormFieldCommand;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoFormRestClient implements MarketoFormClient {
    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoFormRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Form> listForms(int offset, int limit) throws MarketoApiException {
        final List<Form> forms = listForms(offset, limit, null, null);
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public List<Form> listForms(int offset, int limit, FolderId folder, Email.Status status) throws MarketoApiException {
        return httpCommandExecutor.execute(new GetFormsCommand(offset, limit, folder, status));
    }

    @Override //TODO check
    public Form loadFormById(int id) throws MarketoApiException {
        List<Form> execute = httpCommandExecutor.execute(new LoadFormByIdCommand(id));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("Form[id = %d] not found", id));
        }
    }

    @Override
    public List<Form> getFormsByName(final String name, FolderId folder, Form.Status status) throws MarketoApiException {
        final List<Form> forms = httpCommandExecutor.execute(new GetFormsByNameCommand(name, folder, status));
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public List<FormField> loadFormFields(int formId) throws MarketoApiException {
        return httpCommandExecutor.execute(new LoadFormFields(formId));
    }

    @Override
    public Form cloneForm(int sourceFormId, String newFormName, FolderId folderId, String description) throws MarketoApiException {
        List<Form> cloned = httpCommandExecutor.execute(new CloneFormCommand(sourceFormId, newFormName, folderId, description));
        return cloned.get(0);
    }

    @Override
    public Form cloneForm(Form existingForm, String newFormName) throws MarketoApiException {
        return cloneForm(existingForm.getId(), newFormName, new FolderId(existingForm.getFolder()), existingForm.getDescription());
    }

    @Override
    public void updateForm(Form form) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateFormCommand(form));
    }

    @Override
    public void updateFormFields(int formId, List<FormField> formFields) throws MarketoApiException {
        for (FormField formField : formFields) {
            updateFormField(formId, formField);
        }
    }

    @Override
    public void updateFormField(int formId, FormField formField) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateFormFieldCommand(formId, formField));
    }
}
