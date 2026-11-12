package com.smartling.marketo.sdk.rest.command.v2.email;

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CloneEmailTest
{
    private static final String EMAIL_ID = "1001";
    private static final String NEW_NAME = "Test Email Copy";

    @Test
    public void shouldTargetCloneEndpointWithAssetIdAndNewName()
    {
        CloneEmail command = new CloneEmail(EMAIL_ID, NEW_NAME);

        Map<String, Object> newAsset = new LinkedHashMap<>();
        newAsset.put("name", NEW_NAME);
        Map<String, Object> expectedBody = new LinkedHashMap<>();
        expectedBody.put("assetId", EMAIL_ID);
        expectedBody.put("newAsset", newAsset);

        assertThat(command.getPath()).isEqualTo("/asset/v2/email/clone");
        assertThat(command.getMethod()).isEqualTo("JSON_POST");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getParameters()).isEmpty();
        assertThat(command.getBody()).isEqualTo(expectedBody);
        assertThat(command.getResultType().getTypeName())
                .isEqualTo("java.util.List<com.smartling.marketo.sdk.domain.v2.email.EmailV2>");
    }
}
