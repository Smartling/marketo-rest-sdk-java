package com.smartling.marketo.sdk.domain.landingpage;

public abstract class LandingPageContentItem {
    private String id;
    private String type;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return "LandingPageContentItem{" + "id='" + id + '\'' + ", type='" + type + '\'' + ", index=" + index + '}';
    }
}
