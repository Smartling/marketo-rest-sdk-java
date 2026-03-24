package com.smartling.marketo.sdk.domain.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailSubjectDeserializationTest
{

    private final ObjectMapper objectMapper = new ObjectMapperProvider().getContext(EmailSubjectDeserializationTest.class);

    @Test
    public void shouldDeserializeTextSubject() throws Exception
    {
        String json = "{\"subject\": {\"type\": \"Text\", \"value\": \"Hello World\"}}";

        Email email = objectMapper.readValue(json, Email.class);

        assertThat(email.getSubjectField()).isInstanceOf(TextField.class);
        assertThat(email.getSubject()).isEqualTo("Hello World");
        assertThat(email.getSubjectType()).isEqualTo("Text");
    }

    @Test
    public void shouldDeserializeDynamicContentSubject() throws Exception
    {
        String json = "{\"subject\": {\"type\": \"DynamicContent\", \"value\": \"SC1zdWJqZWN0\"}}";

        Email email = objectMapper.readValue(json, Email.class);

        assertThat(email.getSubjectField()).isInstanceOf(DynamicContentField.class);
        assertThat(email.getSubject()).isEqualTo("SC1zdWJqZWN0");
        assertThat(email.getSubjectType()).isEqualTo("DynamicContent");
    }

    @Test
    public void shouldDeserializeDynamicContentFromName() throws Exception
    {
        String json = "{\"fromName\": {\"type\": \"DynamicContent\", \"value\": \"SC1mcm9tTmFtZQ==\"}}";

        Email email = objectMapper.readValue(json, Email.class);

        assertThat(email.getFromNameField()).isInstanceOf(DynamicContentField.class);
        assertThat(email.getFromName()).isEqualTo("SC1mcm9tTmFtZQ==");
        assertThat(email.getFromNameType()).isEqualTo("DynamicContent");
    }
}
