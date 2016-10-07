package com.smartling.marketo.sdk.domain;

public abstract class Asset extends BaseEntity{
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        DRAFT, APPROVED, DELETED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
