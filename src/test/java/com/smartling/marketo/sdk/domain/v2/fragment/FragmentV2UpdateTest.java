package com.smartling.marketo.sdk.domain.v2.fragment;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FragmentV2UpdateTest
{
    private static final String HTML_BODY = "<html><body><h1>Hello</h1></body></html>";
    private static final String TEXT_BODY = "Hello";

    @Test
    public void shouldRejectUpdateWithoutAnyPart()
    {
        FragmentV2Update.Builder builder = FragmentV2Update.builder();

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one");
    }

    @Test
    public void shouldRejectUpdateWhenEveryPartIsNull()
    {
        FragmentV2Update.Builder builder = FragmentV2Update.builder()
                .htmlBody(null)
                .textBody(null);

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one");
    }

    @Test
    public void shouldKeepHtmlBodyOnly()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody(HTML_BODY).build();

        assertThat(update)
                .extracting(FragmentV2Update::getHtmlBody, FragmentV2Update::getTextBody)
                .containsExactly(HTML_BODY, null);
    }

    @Test
    public void shouldKeepTextBodyOnly()
    {
        FragmentV2Update update = FragmentV2Update.builder().textBody(TEXT_BODY).build();

        assertThat(update)
                .extracting(FragmentV2Update::getHtmlBody, FragmentV2Update::getTextBody)
                .containsExactly(null, TEXT_BODY);
    }

    @Test
    public void shouldKeepBothParts()
    {
        FragmentV2Update update = FragmentV2Update.builder()
                .htmlBody(HTML_BODY)
                .textBody(TEXT_BODY)
                .build();

        assertThat(update)
                .extracting(FragmentV2Update::getHtmlBody, FragmentV2Update::getTextBody)
                .containsExactly(HTML_BODY, TEXT_BODY);
    }

    @Test
    public void shouldTreatEmptyStringAsSet()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody("").build();

        assertThat(update)
                .extracting(FragmentV2Update::getHtmlBody, FragmentV2Update::getTextBody)
                .containsExactly("", null);
    }
}
