package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TransitionFragmentStateTest
{
    private static final String FRAGMENT_ID = "abc123";
    private static final String PATH = "/asset/v2/fragment/state/transition";

    @Test
    public void shouldApproveWithoutResult()
    {
        TransitionFragmentState<Void> command = TransitionFragmentState.approve(FRAGMENT_ID);

        assertThat(command.getPath()).isEqualTo(PATH);
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getBody()).isEqualTo(body("approve"));
        assertThat(command.getResultType()).isEqualTo(Void.TYPE);
    }

    @Test
    public void shouldCreateDraftWithFragmentListResult()
    {
        TransitionFragmentState<List<FragmentV2>> command = TransitionFragmentState.createDraft(FRAGMENT_ID);

        assertThat(command.getPath()).isEqualTo(PATH);
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getBody()).isEqualTo(body("create_draft"));
        assertThat(command.getResultType().getTypeName())
                .isEqualTo("java.util.List<com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2>");
    }

    private static Map<String, Object> body(String action)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("contentId", FRAGMENT_ID);
        body.put("action", action);

        return body;
    }
}
