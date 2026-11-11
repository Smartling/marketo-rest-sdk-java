package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2Update;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateEmailTest
{
    private static final String EMAIL_ID = "6873";
    private static final String SUBJECT = "Test Subject";
    private static final String FROM_NAME = "John Doe";
    private static final String HTML_BODY = "<html><body><h1>Hello</h1></body></html>";
    private static final String TEXT_BODY = "Hello";

    @Test
    public void shouldTargetEmailUpdateEndpoint()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().subject(SUBJECT).build());

        assertThat(command.getPath()).isEqualTo("/asset/v2/email/6873/update");
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getResultType()).isEqualTo(Void.TYPE);
    }

    @Test
    public void shouldSendAllFourParts()
    {
        EmailV2Update update = EmailV2Update.builder()
                .subject(SUBJECT)
                .fromName(FROM_NAME)
                .htmlBody(HTML_BODY)
                .textBody(TEXT_BODY)
                .build();

        // Build the update command
        UpdateEmail command = new UpdateEmail(EMAIL_ID, update);

        assertThat(command.getBody()).isEqualTo(map(
                "headers", map("subject", SUBJECT, "fromName", FROM_NAME),
                "data", map("html", map("body", HTML_BODY), "text", map("body", TEXT_BODY))));
    }

    @Test
    public void shouldSendOnlySubject()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().subject(SUBJECT).build());

        assertThat(command.getBody()).isEqualTo(map("headers", map("subject", SUBJECT)));
    }

    @Test
    public void shouldSendOnlyFromName()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().fromName(FROM_NAME).build());

        assertThat(command.getBody()).isEqualTo(map("headers", map("fromName", FROM_NAME)));
    }

    @Test
    public void shouldSendOnlyHtmlBody()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().htmlBody(HTML_BODY).build());

        assertThat(command.getBody()).isEqualTo(map("data", map("html", map("body", HTML_BODY))));
    }

    @Test
    public void shouldSendOnlyTextBody()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().textBody(TEXT_BODY).build());

        assertThat(command.getBody()).isEqualTo(map("data", map("text", map("body", TEXT_BODY))));
    }

    @Test
    public void shouldSendEmptyStringPart()
    {
        UpdateEmail command = new UpdateEmail(EMAIL_ID, EmailV2Update.builder().subject("").build());

        assertThat(command.getBody()).isEqualTo(map("headers", map("subject", "")));
    }

    private static Map<String, Object> map(Object... keysAndValues)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            result.put((String) keysAndValues[i], keysAndValues[i + 1]);
        }
        return result;
    }
}
