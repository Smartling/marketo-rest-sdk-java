package com.smartling.marketo.sdk.domain.snippet;

import com.smartling.marketo.sdk.domain.Asset;

public class Snippet extends Asset {
    private String workspace;

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    @Override
    public String toString()
    {
        return "Snippet{" + "workspace='" + workspace + '\'' + "} " + super.toString();
    }
}
