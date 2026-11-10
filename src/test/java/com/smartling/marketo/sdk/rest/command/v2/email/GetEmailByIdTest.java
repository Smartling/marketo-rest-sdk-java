package com.smartling.marketo.sdk.rest.command.v2.email;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class GetEmailByIdTest
{
    private static final String EMAIL_ID = "6873";

    @Test
    public void shouldTargetEmailByIdEndpoint()
    {
        GetEmailById command = new GetEmailById(EMAIL_ID);

        assertThat(command.getPath()).isEqualTo("/asset/v2/email/6873");
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getResultType().getTypeName())
                .isEqualTo("java.util.List<com.smartling.marketo.sdk.domain.v2.email.EmailV2>");
    }
}
