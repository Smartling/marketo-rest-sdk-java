package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateFragmentTest
{
    private static final String FRAGMENT_ID = "abc123";
    private static final String HTML_BODY = "<p>Hello</p>";
    private static final String TEXT_BODY = "Hello";

    @Test
    public void shouldTargetFragmentUpdateEndpoint()
    {
        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(HTML_BODY).build());

        assertThat(command.getPath()).isEqualTo("/asset/v2/fragment/abc123/update");
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getResultType()).isEqualTo(Void.TYPE);
    }

    @Test
    public void shouldSendBothParts()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody(HTML_BODY).textBody(TEXT_BODY).build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(map("data", map("html", map("body", HTML_BODY), "text", map("body", TEXT_BODY))));
    }

    @Test
    public void shouldSendOnlyHtmlBody()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody(HTML_BODY).build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(map("data", map("html", map("body", HTML_BODY))));
    }

    @Test
    public void shouldSendOnlyTextBody()
    {
        FragmentV2Update update = FragmentV2Update.builder().textBody(TEXT_BODY).build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(map("data", map("text", map("body", TEXT_BODY))));
    }

    @Test
    public void shouldSendEmptyStringAsSet()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody("").build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(map("data", map("html", map("body", ""))));
    }

    @Test
    public void shouldNeverSendSyncFromHtml()
    {
        UpdateFragment htmlOnlyCommand = new UpdateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody(HTML_BODY).build());
        UpdateFragment textOnlyCommand = new UpdateFragment(FRAGMENT_ID, FragmentV2Update.builder().textBody(TEXT_BODY).build());
        UpdateFragment bothPartsCommand = new UpdateFragment(
                FRAGMENT_ID,
                FragmentV2Update.builder().htmlBody(HTML_BODY).textBody(TEXT_BODY).build());

        assertThat(htmlOnlyCommand.getBody().toString()).doesNotContain("syncFromHtml");
        assertThat(textOnlyCommand.getBody().toString()).doesNotContain("syncFromHtml");
        assertThat(bothPartsCommand.getBody().toString()).doesNotContain("syncFromHtml");
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
