package com.smartling.marketo.sdk.domain.landingpage;

import java.util.List;

public class DynamicContent {
    private Integer assetId;
    private Integer segmentation;
    private List<DynamicContentItem> segments;

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

    public List<DynamicContentItem> getSegments() {
        return segments;
    }

    public void setSegments(List<DynamicContentItem> segments) {
        this.segments = segments;
    }
}
