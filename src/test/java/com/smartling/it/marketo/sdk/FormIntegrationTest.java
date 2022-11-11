package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFormClient;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.form.FieldMetaData;
import com.smartling.marketo.sdk.domain.form.SelectFormField;
import com.smartling.marketo.sdk.domain.form.SelectItemValue;
import com.smartling.marketo.sdk.domain.form.Value;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.domain.form.Form.KnownVisitor;
import com.smartling.marketo.sdk.domain.form.FormField;
import com.smartling.marketo.sdk.domain.form.PickListDTO;
import com.smartling.marketo.sdk.domain.form.RuleType;
import com.smartling.marketo.sdk.domain.form.VisibilityRules;
import com.smartling.marketo.sdk.domain.form.VisibilityRulesParameter;
import com.smartling.marketo.sdk.rest.command.form.UpdateFieldPosition;
import com.smartling.marketo.sdk.rest.command.form.UpdateFieldPositionsList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.smartling.marketo.sdk.domain.Asset.Status.APPROVED;
import static com.smartling.marketo.sdk.domain.Asset.Status.DRAFT;
import static com.smartling.marketo.sdk.domain.form.FormFieldType.FIELDSET;
import static com.smartling.marketo.sdk.domain.form.FormFieldType.HTMLTEXT;
import static com.smartling.marketo.sdk.domain.form.RuleType.SHOW;
import static org.assertj.core.api.Assertions.assertThat;

public class FormIntegrationTest extends BaseIntegrationTest {
    private static final int TEST_FORM_ID = 1012;
    private static final String TEST_FORM_NAME = "Form For Integration Tests";
    private static final FolderId TEST_FOLDER_ID = new FolderId(123, FolderType.FOLDER);
    private static final int TEST_PROGRAM_FORM_ID = 1013;
    private static final FolderId TEST_PROGRAM_ID = new FolderId(1008, FolderType.PROGRAM);
    private static final String TEST_FORM_FIELD = "MarketoSocialGender";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private MarketoFormClient marketoFormClient;

    @Before
    public void setUp() {
        marketoFormClient = marketoClientManager.getMarketoFormClient();
    }

    @Test
    public void shouldGetFormsWithFilter() throws Exception {
        List<Form> forms = marketoFormClient.getForms(0, 1, TEST_FOLDER_ID, APPROVED);

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
        List<Form> forms = marketoFormClient.getForms(10000, 1, null, null);

        assertThat(forms).isEmpty();
    }

    @Test(expected = MarketoApiException.class)
    public void shouldThrowLogicException() throws Exception {
        marketoFormClient.getForms(-5, 5, null, null);
    }

    @Test
    public void shouldGetFormById() throws Exception {
        Form form = marketoFormClient.getFormById(TEST_FORM_ID);

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

        marketoFormClient.getFormById(42);
    }

    @Test
    public void shouldGetFormsByName() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, null, null);

        assertThat(forms).haveAtLeast(1, new EntityWithName(TEST_FORM_NAME));
    }

    @Test
    public void shouldGetFormsByNameWithFolder() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, TEST_FOLDER_ID, null);

        assertThat(forms).haveAtLeast(1, new EntityWithNameAndFolderId(TEST_FORM_NAME, TEST_FOLDER_ID));
    }

    @Test
    public void shouldGetFormsByNameWithStatus() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, null, APPROVED);

        assertThat(forms).haveAtLeast(1, new AssetWithNameAndStatus(TEST_FORM_NAME, APPROVED));
    }

    @Test
    public void shouldGetFormsByNameWithPagination() throws Exception {
        List<Form> forms = marketoFormClient.getFormsByName(TEST_FORM_NAME, null, null);
        assertThat(forms.size() > 1).isTrue();

        List<Form> paginatedForms = marketoFormClient.getFormsByName(0, 1, TEST_FORM_NAME, null, null);
        assertThat(paginatedForms.size()).isEqualTo(1);
    }

    @Test
    public void shouldReadFormFields() throws Exception {
        List<FormField> formFields = marketoFormClient.getFormFields(TEST_FORM_ID, APPROVED);

        assertThat(formFields).hasSize(24);
        FormField formField = formFields.get(0);
        assertThat(formField.getId()).isEqualTo("FirstName");
        assertThat(formField.getLabel()).isEqualTo("Int test label");
        assertThat(formField.getInstructions()).isEqualTo("Integration test field instruction");
        assertThat(formField.getDataType()).isEqualTo("text");
        assertThat(formField.getValidationMessage()).isEqualTo("Integration test validation message");
        assertThat(formField.getHintText()).isEqualTo("Your first name here, please");
        assertThat(formField.getVisibilityRules().getRuleType()).isEqualTo(RuleType.ALWAYSSHOW);

        VisibilityRules visibilityRules = formFields.get(3).getVisibilityRules();
        assertThat(visibilityRules.getRuleType()).isEqualTo(SHOW);
        assertThat(visibilityRules.getRules().get(0).getSubjectField()).isEqualTo("FirstName");
        assertThat(visibilityRules.getRules().get(0).getAltLabel()).isEqualTo("Address:");

        formField = formFields.get(21);
        assertThat(formField.getFieldMetaData()).isNotNull();
        assertThat(formField.getFieldMetaData().getValues()).isNotNull();
        List<Value> dropdownValues = formField.getFieldMetaData().getValues();
        assertThat(dropdownValues).hasSizeGreaterThanOrEqualTo(5);
        assertThat(dropdownValues.get(0).getLabel()).isEqualTo("Select...");
        assertThat(dropdownValues.get(2).getLabel()).isEqualTo("Male");
    }

    @Test
    public void shouldReadSelectForm() throws Exception {
        SelectFormField formField = marketoFormClient.getFormSelectField(TEST_FORM_ID, "MarketoSocialGender");

        assertThat(formField.getId()).isEqualTo("MarketoSocialGender");
        assertThat(formField.getLabel()).isEqualTo("Marketo Social Gender:");
        assertThat(formField.getDataType()).isEqualTo("select");
        assertThat(formField.getValidationMessage()).isEqualTo("This field is required.");
        assertThat(formField.getVisibilityRules().getRuleType()).isEqualTo(RuleType.SHOW);

        VisibilityRules visibilityRules = formField.getVisibilityRules();
        assertThat(visibilityRules.getRules().get(0).getSubjectField()).isEqualTo("FirstName");
        assertThat(visibilityRules.getRules().get(0).getAltLabel()).isEqualTo("Marketo Social Gender:");

        assertThat(formField.getFieldMetaData()).isNotNull();
        assertThat(formField.getFieldMetaData().getValues()).isNotNull();
        List<SelectItemValue> dropdownValues = formField.getFieldMetaData().getValues();
        assertThat(dropdownValues).hasSizeGreaterThanOrEqualTo(5);
        assertThat(dropdownValues.get(0).getLabel()).isEqualTo("Select...");
        assertThat(dropdownValues.get(0).getValue()).isEmpty();
        assertThat(dropdownValues.get(0).isDefault()).isTrue();
        assertThat(dropdownValues.get(0).isSelected()).isTrue();
        assertThat(dropdownValues.get(2).getLabel()).isEqualTo("Male");
        assertThat(dropdownValues.get(2).getValue()).isEqualTo("Male");
        assertThat(dropdownValues.get(0).isDefault()).isFalse();
        assertThat(dropdownValues.get(0).isSelected()).isFalse();
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
        Form existingForm = marketoFormClient.getFormById(TEST_FORM_ID);

        Form clone = marketoFormClient.cloneForm(existingForm, newFormName);

        assertThat(clone).isNotNull();
        assertThat(clone.getId()).isPositive();
        assertThat(clone.getName()).isEqualTo(newFormName);
        assertThat(clone.getFolder().getValue()).isEqualTo(existingForm.getFolder().getValue());
    }

    @Test
    public void shouldUpdateTextField() throws Exception {
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
    public void shouldUpdateDropdownList() throws Exception {
        FormField formField = new FormField();
        formField.setId("MarketoSocialGender");
        formField.setLabel("Marketo Sociaux De Genre");
        formField.setValidationMessage("Ce champ est obligatoire");
        formField.setFieldMetaData(fieldMetaData());

        marketoFormClient.updateFormFields(TEST_FORM_ID, Collections.singletonList(formField));

        // Can not verify - no way to fetch not approved content
    }

    private FieldMetaData fieldMetaData() {
        FieldMetaData fieldMetaData = new FieldMetaData();
        List<Value> values = new ArrayList<>();
        values.add(metaDataValue("Sélectionnez...", ""));
        values.add(metaDataValue("Femme", "f"));
        values.add(metaDataValue("Homme.", "m"));
        values.add(metaDataValue("D'autres", "o"));
        values.add(metaDataValue("Je ne sais pas\n", "dk"));
        fieldMetaData.setValues(values);
        return fieldMetaData;
    }

    private Value metaDataValue(String label, String value) {
        Value metaDataValue = new Value();
        metaDataValue.setLabel(label);
        metaDataValue.setValue(value);
        return metaDataValue;
    }

    @Test
    public void shouldUpdateForm() throws Exception {

        Form form = new Form();
        form.setId(TEST_FORM_ID);
        form.setLanguage("English");
        form.setLocale("en_US");

        KnownVisitor knownVisitor = new KnownVisitor();
        knownVisitor.setType(Form.KnownVisitorType.CUSTOM);
        knownVisitor.setTemplate("hello");

        form.setKnownVisitor(knownVisitor);

        marketoFormClient.updateForm(form);

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateSubmitButton() throws Exception {
        marketoFormClient.updateSubmitButton(TEST_FORM_ID, "New button label", "New waiting message");

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldUpdateFormFieldVisibilityRules() throws Exception {
        VisibilityRulesParameter visibilityRules = new VisibilityRulesParameter();
        visibilityRules.setRuleType(SHOW);

        List<VisibilityRulesParameter.Rule> rules = Collections.singletonList(rule("FirstName", "isNotEmpty", "Gender", picklist()));
        visibilityRules.setRules(rules);

        marketoFormClient.updateFormFieldVisibilityRules(TEST_FORM_ID, TEST_FORM_FIELD, visibilityRules);

        // Can not verify - no way to fetch not approved content
    }

    @Test
    public void shouldReArrangeFormFields() throws Exception {
        List<FormField> formFields = marketoFormClient.getFormFields(TEST_FORM_ID, DRAFT);

        UpdateFieldPositionsList updateFieldPositions = createUpdateFieldPositions(formFields);

        marketoFormClient.reArrangeFormFields(TEST_FORM_ID, updateFieldPositions);

        marketoFormClient.discardFormDraft(TEST_FORM_ID);
    }

    @Test
    public void shouldAddAndDeleteRichtextField() throws Exception {
        String richTextValue = "new richText value " + UUID.randomUUID();

        FormField newField = marketoFormClient.addFormRichTextField(TEST_FORM_ID, richTextValue);

        assertThat(
                marketoFormClient.getFormFields(TEST_FORM_ID, DRAFT)
                        .stream()
                        .filter(f -> f.getDataType().equals(HTMLTEXT.getCode()))
                        .filter(f -> f.getText().equals(richTextValue))
                        .findAny()
        ).isNotEmpty();

        marketoFormClient.deleteFormField(TEST_FORM_ID, newField.getId());

        assertThat(
                marketoFormClient.getFormFields(TEST_FORM_ID, DRAFT)
                        .stream()
                        .filter(f -> f.getDataType().equals(HTMLTEXT.getCode()))
                        .filter(f -> f.getText().equals(richTextValue))
                        .findAny()
        ).isEmpty();
    }

    @Test
    public void shouldReplaceRichTextField() throws Exception {
        List<FormField> formFields = marketoFormClient.getFormFields(TEST_FORM_ID, APPROVED);

        FormField originalRichText = formFields.stream()
                .filter(f -> f.getDataType().equals(HTMLTEXT.getCode()))
                .filter(f -> f.getText().contains("<h1>The Rich Text Field</h1>"))
                .findFirst()
                .orElseThrow(() -> new Exception("Not found expected richTextField"));

        FormField newRichText = marketoFormClient.addFormRichTextField(TEST_FORM_ID, "new richText value");

        List<FormField> renewedFormFields = marketoFormClient.getFormFields(TEST_FORM_ID, DRAFT);

        UpdateFieldPositionsList positions = createUpdateFieldPositions(renewedFormFields);

        swapFieldInPositions(positions, originalRichText.getId(), newRichText.getId());

        marketoFormClient.reArrangeFormFields(TEST_FORM_ID, positions);

        marketoFormClient.deleteFormField(TEST_FORM_ID, originalRichText.getId());

        marketoFormClient.discardFormDraft(TEST_FORM_ID);
    }

    private void swapFieldInPositions(List<UpdateFieldPosition> positions, String originalFieldId, String newFieldId) {
        for (UpdateFieldPosition position : positions) {
            if (position.getFieldList() != null) {
                swapFieldInPositions(position.getFieldList(), originalFieldId, newFieldId);
            } else if (position.getFieldName().equals(originalFieldId)) {
                position.setFieldName(newFieldId);
            } else if (position.getFieldName().equals(newFieldId)) {
                position.setFieldName(originalFieldId);
            }
        }
    }

    private List<PickListDTO> picklist()
    {
        List<PickListDTO> picklist = new ArrayList<>();
        picklist.add(pickitem("Sélectionnez...", null));
        picklist.add(pickitem("La Femme", "f"));
        picklist.add(pickitem("La Homme", "m"));
        picklist.add(pickitem("D'autres", "o"));
        picklist.add(pickitem("Je ne sais pas", "dk"));
        return picklist;
    }

    private PickListDTO pickitem(String label, String value)
    {
        PickListDTO pickitem = new PickListDTO();
        pickitem.setLabel(label);
        pickitem.setValue(value);
        return pickitem;
    }

    private VisibilityRulesParameter.Rule rule(String subjectField, String operator, String altLabel, List<PickListDTO> picklist) {
        VisibilityRulesParameter.Rule rule = new VisibilityRulesParameter.Rule();
        rule.setSubjectField(subjectField);
        rule.setOperator(operator);
        rule.setValues(Collections.emptyList());
        rule.setAltLabel(altLabel);
        rule.setPickListValues(picklist);
        return rule;
    }

    private UpdateFieldPositionsList createUpdateFieldPositions(List<FormField> formFields) {
        Map<String, UpdateFieldPosition> simpleFieldsPositions = simpleFieldsPositions(formFields);
        List<UpdateFieldPosition> fieldSetsPositions = fieldSetsPositions(formFields, simpleFieldsPositions);
        List<UpdateFieldPosition> rootFieldsPositions = rootFieldsPositions(simpleFieldsPositions, fieldSetsPositions);


        List<UpdateFieldPosition> fieldPositions = Stream.concat(rootFieldsPositions.stream(), fieldSetsPositions.stream())
                .sorted(Comparator.comparing(UpdateFieldPosition::getRowNumber))
                .collect(Collectors.toList());

        UpdateFieldPositionsList updateFieldPositions = new UpdateFieldPositionsList();
        updateFieldPositions.addAll(fieldPositions);
        return updateFieldPositions;
    }

    private List<UpdateFieldPosition> rootFieldsPositions(Map<String, UpdateFieldPosition> simpleFieldsPositions, List<UpdateFieldPosition> fieldSetsPositions) {
        return simpleFieldsPositions.values().stream()
                .filter(f -> fieldSetsPositions.stream()
                        .noneMatch(fs -> fs.getFieldList().contains(f))
                )
                .collect(Collectors.toList());
    }

    private List<UpdateFieldPosition> fieldSetsPositions(List<FormField> formFields, Map<String, UpdateFieldPosition> fieldPositions) {
        return formFields.stream()
                .filter(fs -> fs.getDataType().equalsIgnoreCase(FIELDSET.getCode()))
                .map(fs -> {
                    UpdateFieldPosition fieldSetPosition = new UpdateFieldPosition();
                    fieldSetPosition.setFieldName(fs.getId());
                    fieldSetPosition.setColumnNumber(fs.getColumnNumber());
                    fieldSetPosition.setRowNumber(fs.getRowNumber());
                    fieldSetPosition.setFieldList(fs.getFields().stream()
                            .map(fieldPositions::get)
                            .collect(Collectors.toList())
                    );
                    return fieldSetPosition;
                })
                .collect(Collectors.toList());
    }

    private Map<String, UpdateFieldPosition> simpleFieldsPositions(List<FormField> formFields) {
        return formFields.stream()
                .filter(f -> !f.getDataType().equalsIgnoreCase(FIELDSET.getCode()))
                .map(f -> {
                    UpdateFieldPosition fieldPosition = new UpdateFieldPosition();
                    fieldPosition.setFieldName(f.getId());
                    fieldPosition.setColumnNumber(f.getColumnNumber());
                    fieldPosition.setRowNumber(f.getRowNumber());
                    return fieldPosition;
                })
                .collect(Collectors.toMap(UpdateFieldPosition::getFieldName, p -> p));
    }
}
