package com.smartling.marketo.sdk.domain.v2.fragment;

/**
 * Immutable set of changes for the Asset API v2 fragment update endpoint. At least one part is set; an unset part is
 * {@code null} and is not sent, while an empty string counts as set. {@code syncFromHtml} is not sent: Marketo does
 * not regenerate the text body from the HTML body, so a caller that changes the HTML sends the text body too.
 */
public final class FragmentV2Update
{
    private final String htmlBody;
    private final String textBody;

    private FragmentV2Update(Builder builder)
    {
        this.htmlBody = builder.htmlBody;
        this.textBody = builder.textBody;
    }

    public static Builder builder()
    {
        return new Builder();
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
        private String htmlBody;
        private String textBody;

        private Builder()
        {
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

        public FragmentV2Update build()
        {
            // An update without any part would send an empty request
            if (htmlBody == null && textBody == null)
            {
                throw new IllegalArgumentException("at least one part of the update must be set");
            }

            return new FragmentV2Update(this);
        }
    }
}
