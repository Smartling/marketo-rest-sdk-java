package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.domain.form.VisibilityRulesParameter;
import com.smartling.marketo.sdk.rest.command.form.UpdateFieldPositionsList;

import java.util.List;

public interface MarketoFormClient {
    List<Form> getForms(Integer offset, Integer limit, FolderId folder, Email.Status status) throws MarketoApiException;

    Form getFormById(int id) throws MarketoApiException;

    List<Form> getFormsByName(String name, FolderId folder, Form.Status status) throws MarketoApiException;

    List<Form> getFormsByName(Integer offset, Integer limit, String name, FolderId folder, Form.Status status) throws MarketoApiException;

    List<FormField> getFormFields(int formId, Asset.Status status) throws MarketoApiException;

    Form cloneForm(int sourceFormId, String newFormName, FolderId folderId, String description) throws MarketoApiException;

    Form cloneForm(Form existingForm, String newFormName) throws MarketoApiException;

    void updateForm(Form form) throws MarketoApiException;

    void updateFormFields(int formId, List<FormField> formFields) throws MarketoApiException;

    void updateFormField(int formId, FormField formField) throws MarketoApiException;

    void discardFormDraft(int formId) throws MarketoApiException;

    void deleteFormField(int formId, String fieldId) throws MarketoApiException;

    FormField addFormField(int formId, FormField formField) throws MarketoApiException;

    FormField addFormFieldSet(int formId, String label) throws MarketoApiException;

    FormField addFormRichTextField(int formId, String text) throws MarketoApiException;

    void reArrangeFormFields(int formId, UpdateFieldPositionsList positions) throws MarketoApiException;

    void updateSubmitButton(int formId, String label, String waitingLabel) throws MarketoApiException;

    void updateFormFieldVisibilityRules(int formId, String formField, VisibilityRulesParameter visibilityRules) throws MarketoApiException;

    void approveDraft(int id) throws MarketoApiException;
}
