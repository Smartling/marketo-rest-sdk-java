package com.smartling.marketo.sdk.rest.command;

import com.google.common.collect.Lists;
import com.smartling.marketo.sdk.EmailContentItem;
import com.smartling.marketo.sdk.EmailTextContentItem;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;


public class UpdateEmailEditableSectionTest {
    @Test
    public void shouldBuildRequestPath() throws Exception {
        EmailContentItem contentItem = new EmailTextContentItem();
        contentItem.setHtmlId("test");

        String path = new UpdateEmailEditableSection(42, contentItem).getPath();

        assertThat(path).isEqualTo("/asset/v1/email/42/content/test.json");
    }

    @Test
    public void shouldAddContentTypeParameter() throws Exception {
        EmailContentItem contentItem = new EmailTextContentItem();
        contentItem.setContentType(EmailContentItem.ContentType.TEXT);

        Map<String, Object> parameters = new UpdateEmailEditableSection(42, contentItem).getParameters();

        assertThat(parameters).contains(entry("type", EmailContentItem.ContentType.TEXT));
    }

    @Test
    public void shouldAddTextContentToParameters() throws Exception {
        EmailTextContentItem contentItem = new EmailTextContentItem();
        contentItem.setContentType(EmailContentItem.ContentType.TEXT);
        contentItem.setValue(Collections.singletonList(new EmailTextContentItem.Value()));
        contentItem.getValue().get(0).setType("Text");
        contentItem.getValue().get(0).setValue("text value");

        Map<String, Object> parameters = new UpdateEmailEditableSection(42, contentItem).getParameters();

        assertThat(parameters).contains(entry("textValue", "text value"));
    }

    @Test
    public void shouldAddHtmlContentToParameters() throws Exception {
        EmailTextContentItem contentItem = new EmailTextContentItem();
        contentItem.setContentType(EmailContentItem.ContentType.TEXT);
        contentItem.setValue(Collections.singletonList(new EmailTextContentItem.Value()));
        contentItem.getValue().get(0).setType("HTML");
        contentItem.getValue().get(0).setValue("<b>HTML</b> value");

        Map<String, Object> parameters = new UpdateEmailEditableSection(42, contentItem).getParameters();

        assertThat(parameters).contains(entry("value", "<b>HTML</b> value"));
    }

    @Test
    public void shouldTolerateNullValuesInContentItem(){
        EmailTextContentItem contentItem = new EmailTextContentItem();
        contentItem.setContentType(EmailContentItem.ContentType.TEXT);
        contentItem.setValue(Lists.newArrayList(new EmailTextContentItem.Value(), new EmailTextContentItem.Value()));
        contentItem.getValue().get(0).setType("HTML");
        contentItem.getValue().get(0).setValue(null);
        contentItem.getValue().get(1).setType("Text");
        contentItem.getValue().get(1).setValue(null);

        Map<String, Object> parameters = new UpdateEmailEditableSection(42, contentItem).getParameters();
        assertThat(parameters).contains(entry("value", null));
        assertThat(parameters).contains(entry("textValue", null));
    }
}