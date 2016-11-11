package com.smartling.marketo.sdk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Asset extends BaseEntity{
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        @JsonProperty("draft")
        DRAFT,
        @JsonProperty("approved")
        APPROVED,
        @JsonProperty("deleted")
        DELETED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
