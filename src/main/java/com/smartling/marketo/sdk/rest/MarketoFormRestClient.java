package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.domain.form.VisibilityRules;
import com.smartling.marketo.sdk.rest.command.form.CloneForm;
import com.smartling.marketo.sdk.rest.command.form.GetFormsByName;
import com.smartling.marketo.sdk.rest.command.form.GetForms;
import com.smartling.marketo.sdk.rest.command.form.GetFormById;
import com.smartling.marketo.sdk.rest.command.form.GetFormFields;
import com.smartling.marketo.sdk.rest.command.form.UpdateForm;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormField;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormFieldVisibilityRules;
import com.smartling.marketo.sdk.rest.command.form.UpdateSubmitButton;
import com.smartling.marketo.sdk.rest.transport.HttpCommandExecutor;

import java.util.Collections;
import java.util.List;

public class MarketoFormRestClient implements MarketoFormClient {
    private final HttpCommandExecutor httpCommandExecutor;

    public MarketoFormRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Form> getForms(int offset, int limit, FolderId folder, Status status) throws MarketoApiException {
        final List<Form> forms = httpCommandExecutor.execute(new GetForms(offset, limit, folder, status));
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public Form getFormById(int id) throws MarketoApiException {
        List<Form> execute = httpCommandExecutor.execute(new GetFormById(id));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new MarketoApiException(String.format("Form[id = %d] not found", id));
        }
    }

    @Override
    public List<Form> getFormsByName(final String name, FolderId folder, Status status) throws MarketoApiException {
        final List<Form> forms = httpCommandExecutor.execute(new GetFormsByName(name, folder, status));
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public List<FormField> getFormFields(int formId, Status status) throws MarketoApiException {
        return httpCommandExecutor.execute(new GetFormFields(formId, status));
    }

    @Override
    public Form cloneForm(int sourceFormId, String newFormName, FolderId folderId, String description) throws MarketoApiException {
        List<Form> cloned = httpCommandExecutor.execute(new CloneForm(sourceFormId, newFormName, folderId, description));
        return cloned.get(0);
    }

    @Override
    public Form cloneForm(Form existingForm, String newFormName) throws MarketoApiException {
        return cloneForm(existingForm.getId(), newFormName, new FolderId(existingForm.getFolder()), existingForm.getDescription());
    }

    @Override
    public void updateForm(Form form) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateForm(form));
    }

    @Override
    public void updateFormFields(int formId, List<FormField> formFields) throws MarketoApiException {
        for (FormField formField : formFields) {
            updateFormField(formId, formField);
        }
    }

    @Override
    public void updateFormField(int formId, FormField formField) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateFormField(formId, formField));
    }

    @Override
    public void updateSubmitButton(int formId, String label, String waitingLabel) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateSubmitButton(formId, label, waitingLabel));
    }

    @Override
    public void updateFormFieldVisibilityRules(int formId, String formField, VisibilityRules visibilityRule) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateFormFieldVisibilityRules(formId, formField, visibilityRule));
    }
}
