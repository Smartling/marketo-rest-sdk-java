package com.smartling.marketo.sdk.rest.command.v2.fragment;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class GetFragmentByIdTest
{
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";

    @Test
    public void shouldTargetFragmentByIdEndpoint()
    {
        GetFragmentById command = new GetFragmentById(FRAGMENT_ID);

        assertThat(command.getPath()).isEqualTo("/asset/v2/fragment/0123456789abcdef01234567");
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getResultType().getTypeName())
                .isEqualTo("java.util.List<com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2>");
    }
}
