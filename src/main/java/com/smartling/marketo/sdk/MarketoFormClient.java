package com.smartling.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.email.Email;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.domain.form.VisibilityRules;
import com.smartling.marketo.sdk.domain.form.VisibilityRulesParameter;

import java.util.List;

public interface MarketoFormClient {
    List<Form> getForms(int offset, int limit, FolderId folder, Email.Status status) throws MarketoApiException;

    Form getFormById(int id) throws MarketoApiException;

    List<Form> getFormsByName(String name, FolderId folder, Form.Status status) throws MarketoApiException;

    List<FormField> getFormFields(int formId, Asset.Status status) throws MarketoApiException;

    Form cloneForm(int sourceFormId, String newFormName, FolderId folderId, String description) throws MarketoApiException;

    Form cloneForm(Form existingForm, String newFormName) throws MarketoApiException;

    void updateForm(Form form) throws MarketoApiException;

    void updateFormFields(int formId, List<FormField> formFields) throws MarketoApiException;

    void updateFormField(int formId, FormField formField) throws MarketoApiException;

    void updateSubmitButton(int formId, String label, String waitingLabel) throws MarketoApiException;

    void updateFormFieldVisibilityRules(int formId, String formField, VisibilityRulesParameter visibilityRules) throws MarketoApiException;

    void approveDraft(int id) throws MarketoApiException;
}
