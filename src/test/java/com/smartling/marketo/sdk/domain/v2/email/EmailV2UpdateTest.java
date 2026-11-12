package com.smartling.marketo.sdk.domain.v2.email;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmailV2UpdateTest
{
    private static final String SUBJECT = "Test Subject";
    private static final String FROM_NAME = "John Doe";
    private static final String HTML_BODY = "<html><body><h1>Hello</h1></body></html>";
    private static final String TEXT_BODY = "Hello";

    @Test
    public void shouldRejectUpdateWithoutAnyPart()
    {
        EmailV2Update.Builder builder = EmailV2Update.builder();

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one");
    }

    @Test
    public void shouldRejectUpdateWhenEveryPartIsNull()
    {
        EmailV2Update.Builder builder = EmailV2Update.builder()
                .subject(null)
                .fromName(null)
                .htmlBody(null)
                .textBody(null);

        assertThatThrownBy(builder::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldKeepAllParts()
    {
        EmailV2Update update = EmailV2Update.builder()
                .subject(SUBJECT)
                .fromName(FROM_NAME)
                .htmlBody(HTML_BODY)
                .textBody(TEXT_BODY)
                .build();

        assertThat(update.getSubject()).isEqualTo(SUBJECT);
        assertThat(update.getFromName()).isEqualTo(FROM_NAME);
        assertThat(update.getHtmlBody()).isEqualTo(HTML_BODY);
        assertThat(update.getTextBody()).isEqualTo(TEXT_BODY);
    }

    @Test
    public void shouldLeaveNullPartsUnset()
    {
        EmailV2Update update = EmailV2Update.builder()
                .subject(SUBJECT)
                .htmlBody(null)
                .build();

        assertThat(update.getSubject()).isEqualTo(SUBJECT);
        assertThat(update.getFromName()).isNull();
        assertThat(update.getHtmlBody()).isNull();
        assertThat(update.getTextBody()).isNull();
    }

    @Test
    public void shouldCountEmptyStringAsSet()
    {
        EmailV2Update update = EmailV2Update.builder().textBody("").build();

        assertThat(update.getTextBody()).isEmpty();
    }
}
