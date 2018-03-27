package com.smartling.marketo.sdk.domain.program;

import com.smartling.marketo.sdk.domain.BaseEntity;

import java.util.List;

public class Program extends BaseEntity {

    private String channel;
    private String type;
    private String Status;
    private List<Tag> tags;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString()
    {
        return "Program{" + "channel='" + channel + '\'' + ", type='" + type + '\'' + ", Status='" + Status + '\'' + ", tags=" + tags + "} "
                + super.toString();
    }
}
