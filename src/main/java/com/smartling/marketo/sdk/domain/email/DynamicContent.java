package com.smartling.marketo.sdk.domain.email;

import java.util.List;

public class DynamicContent {
    private Integer assetId;
    private Integer segmentation;
    private List<DynamicContentItem> content;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(Integer segmentation) {
        this.segmentation = segmentation;
    }

    public List<DynamicContentItem> getContent() {
        return content;
    }

    public void setContent(List<DynamicContentItem> content) {
        this.content = content;
    }
}
