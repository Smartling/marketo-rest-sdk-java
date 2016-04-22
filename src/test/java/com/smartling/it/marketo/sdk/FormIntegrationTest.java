package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.domain.Asset.Status;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.MarketoApiException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;

public class FormIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_FORM_ID = 1012;
    private static final String TEST_FORM_NAME = "Form For Integration Tests";
    private static final FolderId TEST_FOLDER_ID = new FolderId(123, FolderType.FOLDER);
    private static final int TEST_PROGRAM_FORM_ID = 1013;
    private static final FolderId TEST_PROGRAM_ID = new FolderId(1008, FolderType.PROGRAM);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private MarketoFormClient marketoFormClient;

    @Before
    public void setUp() {
        marketoFormClient = marketoClientManager.getMarketoFormClient();
    }

    @Test
    public void shouldListForms() throws Exception {
        List<Form> forms = marketoFormClient.listForms(0, 1);

        assertThat(forms).hasSize(1);
        assertThat(forms.get(0).getId()).isPositive();
        assertThat(forms.get(0).getName()).isNotEmpty();
        assertThat(forms.get(0).getUpdatedAt()).isNotNull();
        assertThat(forms.get(0).getStatus()).isNotNull();
        assertThat(forms.get(0).getUrl()).isNotEmpty();
        assertThat(forms.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldListFormsWithFilter() throws Exception {
        List<Form> forms = marketoFormClient.listForms(0, 1, TEST_FOLDER_ID, Status.APPROVED);

        assertThat(forms).hasSize(1);
        assertThat(forms.get(0).getId()).isPositive();
        assertThat(forms.get(0).getName()).isNotEmpty();
        assertThat(forms.get(0).getUpdatedAt()).isNotNull();
        assertThat(forms.get(0).getStatus()).isNotNull();
        assertThat(forms.get(0).getUrl()).isNotEmpty();
        assertThat(forms.get(0).getFolder()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListWhenEndReached() throws Exception {
        List<Form> forms = marketoFormClient.listForms(10000, 1);

        assertThat(forms).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoFormClient.listForms(-5, 5);
    }

    @Test
    public void shouldLoadFormById() throws Exception {
        Form form = marketoFormClient.loadFormById(TEST_FORM_ID);

        assertThat(form).isNotNull();
        assertThat(form.getId()).isEqualTo(TEST_FORM_ID);
        assertThat(form.getName()).isNotEmpty();
        assertThat(form.getUrl()).isNotEmpty();
        assertThat(form.getFolder()).isNotNull();
    }

    @Test
    public void shouldThrowMarketoApiExceptionWhenCouldNotFindFormById() throws Exception {

        thrown.expect(MarketoApiException.class);
        thrown.expectMessage("Form[id = 42] not found");

        marketoFormClient.loadFormById(42);
    }

    @Test
    public void shouldGetFormsByName() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, null, null);

        assertThat(forms).haveAtLeast(1, new AssetWithName(TEST_FORM_NAME));
    }

    @Test
    public void shouldGetFormsByNameWithFolder() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, TEST_FOLDER_ID, null);

        assertThat(forms).haveAtLeast(1, new AssetWithNameAndFolderId(TEST_FORM_NAME, TEST_FOLDER_ID));
    }

    @Test
    public void shouldGetFormsByNameWithStatus() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, null, Status.APPROVED);

        assertThat(forms).haveAtLeast(1, new AssetWithNameAndStatus(TEST_FORM_NAME, Status.APPROVED));
    }

    @Test
    public void shouldReadFormFields() throws Exception {
        List<FormField> formFields = marketoFormClient.loadFormFields(TEST_FORM_ID);

        assertThat(formFields).hasSize(21);
        FormField formField = formFields.get(0);
        assertThat(formField.getId()).isEqualTo("FirstName");
        assertThat(formField.getLabel()).isEqualTo("Int test label");
        assertThat(formField.getInstructions()).isEqualTo("Integration test field instruction");
        assertThat(formField.getDataType()).isEqualTo("text");
        assertThat(formField.getValidationMessage()).isEqualTo("Integration test validation message");
        assertThat(formField.getHintText()).isEqualTo("Your first name here, please");
    }

    @Test
    public void shouldCloneForm() throws Exception {
        String newFormName = "integration-test-clone-" + UUID.randomUUID().toString();

        Form clone = marketoFormClient.cloneForm(TEST_FORM_ID, newFormName, TEST_FOLDER_ID, "");

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newFormName);
        assertThat(new FolderId(clone.getFolder())).isEqualTo(TEST_FOLDER_ID);
    }

    @Test
    public void shouldCloneFormInProgram() throws Exception {
        String newFormName = "integration-test-clone-" + UUID.randomUUID().toString();

        Form clone = marketoFormClient.cloneForm(TEST_PROGRAM_FORM_ID, newFormName, TEST_PROGRAM_ID, "");

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newFormName);
        assertThat(clone.getFolder().getValue()).isEqualTo(TEST_PROGRAM_ID.getId());
    }

    @Test
    public void shouldCloneFormViaShorthandMethod() throws Exception {
        String newFormName = "integration-test-clone-" + UUID.randomUUID().toString();
        Form existingForm = marketoFormClient.loadFormById(TEST_FORM_ID);

        Form clone = marketoFormClient.cloneForm(existingForm, newFormName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newFormName);
        assertThat(clone.getFolder().getValue()).isEqualTo(existingForm.getFolder().getValue());
    }

    @Test
    public void shouldUpdateFormFields() throws Exception {
        FormField formField = new FormField();
        formField.setId("FirstName");
        formField.setDataType("text");
        formField.setLabel("Int test label");
        formField.setInstructions("Integration test field instruction");
        formField.setValidationMessage("Integration test validation message");

        marketoFormClient.updateFormFields(TEST_FORM_ID, Collections.singletonList(formField));

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateForm() throws Exception {

        Form form = new Form();
        form.setId(TEST_FORM_ID);
        form.setName("New name");
        form.setDescription("new description");
        form.setFormLanguage("French");
        form.setFormLocale("French");

        marketoFormClient.updateForm(form);

        // Can not verify - no way to fetch not approved content
    }
}
