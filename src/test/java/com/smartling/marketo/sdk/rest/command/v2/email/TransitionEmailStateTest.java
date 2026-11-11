package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TransitionEmailStateTest
{
    private static final String EMAIL_ID = "6873";
    private static final String PATH = "/asset/v2/email/state/transition";

    @Test
    public void shouldApproveWithoutResult()
    {
        TransitionEmailState<Void> command = TransitionEmailState.approve(EMAIL_ID);

        assertThat(command.getPath()).isEqualTo(PATH);
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getBody()).isEqualTo(body("approve"));
        assertThat(command.getResultType()).isEqualTo(Void.TYPE);
    }

    @Test
    public void shouldCreateDraftWithEmailListResult()
    {
        TransitionEmailState<List<EmailV2>> command = TransitionEmailState.createDraft(EMAIL_ID);

        assertThat(command.getPath()).isEqualTo(PATH);
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getBody()).isEqualTo(body("create_draft"));
        assertThat(command.getResultType().getTypeName())
                .isEqualTo("java.util.List<com.smartling.marketo.sdk.domain.v2.email.EmailV2>");
    }

    private static Map<String, Object> body(String action)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("contentId", EMAIL_ID);
        body.put("action", action);
        return body;
    }
}
