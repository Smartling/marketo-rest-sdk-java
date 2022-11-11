package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.domain.form.SelectFormField;
import com.smartling.marketo.sdk.domain.form.VisibilityRulesParameter;
import com.smartling.marketo.sdk.rest.command.form.ApproveFormDraft;
import com.smartling.marketo.sdk.rest.command.form.CloneForm;
import com.smartling.marketo.sdk.rest.command.form.CreateFormField;
import com.smartling.marketo.sdk.rest.command.form.CreateFormFieldSet;
import com.smartling.marketo.sdk.rest.command.form.CreateFormRichTextField;
import com.smartling.marketo.sdk.rest.command.form.DeleteFormField;
import com.smartling.marketo.sdk.rest.command.form.DiscardFormDraft;
import com.smartling.marketo.sdk.rest.command.form.GetFormById;
import com.smartling.marketo.sdk.rest.command.form.GetFormFields;
import com.smartling.marketo.sdk.rest.command.form.GetForms;
import com.smartling.marketo.sdk.rest.command.form.GetFormsByName;
import com.smartling.marketo.sdk.rest.command.form.GetSelectFormField;
import com.smartling.marketo.sdk.rest.command.form.ReArrangeFormFields;
import com.smartling.marketo.sdk.rest.command.form.UpdateFieldPositionsList;
import com.smartling.marketo.sdk.rest.command.form.UpdateForm;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormField;
import com.smartling.marketo.sdk.rest.command.form.UpdateFormFieldVisibilityRules;
import com.smartling.marketo.sdk.rest.command.form.UpdateSubmitButton;

import java.util.Collections;
import java.util.List;

public class MarketoFormRestClient implements MarketoFormClient {
    private final HttpCommandExecutor httpCommandExecutor;

    MarketoFormRestClient(HttpCommandExecutor httpCommandExecutor) {
        this.httpCommandExecutor = httpCommandExecutor;
    }

    @Override
    public List<Form> getForms(Integer offset, Integer limit, FolderId folder, Status status) throws MarketoApiException {
        List<Form> forms = httpCommandExecutor.execute(new GetForms(offset, limit, folder, status));
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public Form getFormById(int id) throws MarketoApiException {
        List<Form> execute = httpCommandExecutor.execute(new GetFormById(id));
        if (execute != null && !execute.isEmpty()) {
            return execute.get(0);
        } else {
            throw new ObjectNotFoundException(String.format("Form[id = %d] not found", id));
        }
    }

    @Override
    public List<Form> getFormsByName(String name, FolderId folder, Status status) throws MarketoApiException {
        return getFormsByName(null, null, name, folder, status);
    }

    @Override
    public List<Form> getFormsByName(Integer offset, Integer limit, String name, FolderId folder, Status status) throws MarketoApiException {
        List<Form> forms = httpCommandExecutor.execute(new GetFormsByName(offset, limit, name, folder, status));
        return forms != null ? forms : Collections.emptyList();
    }

    @Override
    public List<FormField> getFormFields(int formId, Status status) throws MarketoApiException {
        return httpCommandExecutor.execute(new GetFormFields(formId, status));
    }

    @Override
    public SelectFormField getFormSelectField(int formId, String fieldId) throws MarketoApiException {
        List<SelectFormField> fields = httpCommandExecutor.execute(new GetSelectFormField(formId, fieldId));
        return fields.get(0);
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
    public void discardFormDraft(int formId) throws MarketoApiException {
        httpCommandExecutor.execute(new DiscardFormDraft(formId));
    }

    @Override
    public void deleteFormField(int formId, String fieldId) throws MarketoApiException {
        httpCommandExecutor.execute(new DeleteFormField(formId, fieldId));
    }

    @Override
    public FormField addFormField(int formId, FormField formField) throws MarketoApiException {
        List<FormField> newField = httpCommandExecutor.execute(new CreateFormField(formId, formField));
        return newField.stream()
                .findAny()
                .orElseThrow(() -> new MarketoApiException("Empty response when creating field"));
    }

    @Override
    public FormField addFormFieldSet(int formId, String label) throws MarketoApiException {
        List<FormField> newField = httpCommandExecutor.execute(new CreateFormFieldSet(formId, label));
        return newField.stream()
                .findAny()
                .orElseThrow(() -> new MarketoApiException("Empty response when creating field set"));
    }

    @Override
    public FormField addFormRichTextField(int formId, String text) throws MarketoApiException {
        List<FormField> newField =  httpCommandExecutor.execute(new CreateFormRichTextField(formId, text));
        return newField.stream()
                .findAny()
                .orElseThrow(() -> new MarketoApiException("Empty response when creating richText field"));
    }

    @Override
    public void reArrangeFormFields(int formId, UpdateFieldPositionsList positions) throws MarketoApiException {
        httpCommandExecutor.execute(new ReArrangeFormFields(formId, positions));
    }

    @Override
    public void updateSubmitButton(int formId, String label, String waitingLabel) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateSubmitButton(formId, label, waitingLabel));
    }

    @Override
    public void updateFormFieldVisibilityRules(int formId, String formField, VisibilityRulesParameter visibilityRule) throws MarketoApiException {
        httpCommandExecutor.execute(new UpdateFormFieldVisibilityRules(formId, formField, visibilityRule));
    }

    @Override
    public void approveDraft(int id) throws MarketoApiException {
        httpCommandExecutor.execute(new ApproveFormDraft(id));
    }
}
