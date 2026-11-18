package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetFragmentByIdTest
{
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";

    @Test
    public void shouldTargetFragmentByIdEndpoint()
    {
        GetFragmentById command = new GetFragmentById(FRAGMENT_ID);

        assertThat(command.getPath()).isEqualTo("/asset/v2/fragment/" + FRAGMENT_ID);
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getResultType()).isEqualTo(new TypeToken<List<FragmentV2>>() {}.getType());
    }
}
