package com.smartling.marketo.sdk.rest.command;

import com.smartling.marketo.sdk.Email;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;

public class UpdateEmailContentTest {

    private UpdateEmailContent updateEmailContent = new UpdateEmailContent(prepareDummy());

    private Email prepareDummy() {
        Email email = new Email();
        email.setId(1109);
        email.setSubject("New Subject");
        email.setFromName("From SDK commiter");
        return email;
    }

    @Test
    public void shouldBuildRequestPath() throws Exception {
        String path = updateEmailContent.getPath();
        assertThat(path).isEqualTo("/asset/v1/email/1109/content.json");
    }

    @Test
    public void shouldContainSubject() throws Exception {
        Map<String, Object> parameters = updateEmailContent.getParameters();
        assertThat(parameters).contains(entry("subject", "{\"type\":\"Text\",\"value\":\"New Subject\"}"));
    }

    @Test
    public void shouldContainFromName() throws Exception {
        Map<String, Object> parameters = updateEmailContent.getParameters();
        assertThat(parameters).contains(entry("fromName", "{\"type\":\"Text\",\"value\":\"From SDK commiter\"}"));
    }
}