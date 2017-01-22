package com.smartling.marketo.sdk.domain.landingpagetemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TemplateType {
    @JsonProperty("guided")
    GUIDED,
    @JsonProperty("freeForm")
    FREEFORM
}
