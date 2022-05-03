package com.smartling.marketo.sdk.domain.landingpage;

public class LandingPageFormContentItem extends LandingPageContentItem {
    private String followUpType;

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType;
    }

    @Override
    public String toString()
    {
        return "LandingPageFormContentItem{" +
                    "content=" + content != null ? content.toString() : "" + ", " +
                    "followUpType='" + followUpType + '\'' +
                "} " + super.toString();
    }

    public static class Content {
        private String content;
        private String contentType;
        private String contentUrl;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        @Override
        public String toString() {
            return "Content{content=" + content + ", contentType=" + contentType + ", contentUrl=" + contentUrl + "}";
        }
    }
}
