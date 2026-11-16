package com.smartling.marketo.sdk.domain.v2.email;

/**
 * Immutable set of changes for the Asset API v2 email update endpoint. At least one part is set; an unset part is
 * {@code null} and is not sent, while an empty string counts as set.
 */
public final class EmailV2Update
{
    private final String subject;
    private final String fromName;
    private final String htmlBody;
    private final String textBody;

    private EmailV2Update(Builder builder)
    {
        this.subject = builder.subject;
        this.fromName = builder.fromName;
        this.htmlBody = builder.htmlBody;
        this.textBody = builder.textBody;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String getSubject()
    {
        return subject;
    }

    public String getFromName()
    {
        return fromName;
    }

    public String getHtmlBody()
    {
        return htmlBody;
    }

    public String getTextBody()
    {
        return textBody;
    }

    public static final class Builder
    {
        private String subject;
        private String fromName;
        private String htmlBody;
        private String textBody;

        private Builder()
        {
        }

        public Builder subject(String subject)
        {
            this.subject = subject;
            return this;
        }

        public Builder fromName(String fromName)
        {
            this.fromName = fromName;
            return this;
        }

        public Builder htmlBody(String htmlBody)
        {
            this.htmlBody = htmlBody;
            return this;
        }

        public Builder textBody(String textBody)
        {
            this.textBody = textBody;
            return this;
        }

        public EmailV2Update build()
        {
            // An update without any part would send an empty request
            if (subject == null && fromName == null && htmlBody == null && textBody == null)
            {
                throw new IllegalArgumentException("at least one part of the update must be set");
            }

            return new EmailV2Update(this);
        }
    }
}
