package com.smartling.marketo.sdk.domain.landingpage;

public class LandingPageDynamicContentItem extends LandingPageContentItem {

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "LandingPageDynamicContentItem{" +
                    "content='" + content != null ? content.toString() : "" + '\'' +
                "} " +
                super.toString();
    }

    public static class Content {
        private String content;
        private String contentType;

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

        @Override
        public String toString() {
            return "Content{content=" + content + ", contentType=" + contentType+ "}";
        }
    }
}
