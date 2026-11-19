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

        assertThat(command.getBody()).isEqualTo(body(part("html", HTML_BODY), part("text", TEXT_BODY)));
    }

    @Test
    public void shouldSendOnlyHtmlBody()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody(HTML_BODY).build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(body(part("html", HTML_BODY)));
    }

    @Test
    public void shouldSendOnlyTextBody()
    {
        FragmentV2Update update = FragmentV2Update.builder().textBody(TEXT_BODY).build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(body(part("text", TEXT_BODY)));
    }

    @Test
    public void shouldSendEmptyStringAsSet()
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody("").build();

        UpdateFragment command = new UpdateFragment(FRAGMENT_ID, update);

        assertThat(command.getBody()).isEqualTo(body(part("html", "")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNeverSendSyncFromHtml()
    {
        FragmentV2Update[] updates = {
                FragmentV2Update.builder().htmlBody(HTML_BODY).build(),
                FragmentV2Update.builder().textBody(TEXT_BODY).build(),
                FragmentV2Update.builder().htmlBody(HTML_BODY).textBody(TEXT_BODY).build()
        };

        for (FragmentV2Update update : updates)
        {
            Map<String, Object> body = (Map<String, Object>) new UpdateFragment(FRAGMENT_ID, update).getBody();
            Map<String, Object> data = (Map<String, Object>) body.get("data");

            assertThat(data.values())
                    .allSatisfy(content -> assertThat((Map<String, Object>) content).containsOnlyKeys("body"));
        }
    }

    @SafeVarargs
    private static Map<String, Object> body(Map.Entry<String, Object>... parts)
    {
        Map<String, Object> data = new LinkedHashMap<>();
        for (Map.Entry<String, Object> part : parts)
        {
            data.put(part.getKey(), part.getValue());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("data", data);

        return body;
    }

    private static Map.Entry<String, Object> part(String name, String bodyValue)
    {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("body", bodyValue);

        return new java.util.AbstractMap.SimpleEntry<String, Object>(name, content);
    }
}
