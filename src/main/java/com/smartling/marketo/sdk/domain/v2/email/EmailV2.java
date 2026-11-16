package com.smartling.marketo.sdk.domain.v2.email;

import java.util.Date;
import java.util.List;

public class EmailV2
{
    private String id;
    private String name;
    private String description;
    private String status;
    private String state;
    private String appType;
    private String templateId;
    private String externalId;
    private String virtualId;
    private String scriptEngine;
    private Headers headers;
    private Metadata metadata;
    private AppData appData;
    private Data data;
    private List<AssociatedState> associatedStates;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getAppType()
    {
        return appType;
    }

    public void setAppType(String appType)
    {
        this.appType = appType;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    public String getVirtualId()
    {
        return virtualId;
    }

    public void setVirtualId(String virtualId)
    {
        this.virtualId = virtualId;
    }

    public String getScriptEngine()
    {
        return scriptEngine;
    }

    public void setScriptEngine(String scriptEngine)
    {
        this.scriptEngine = scriptEngine;
    }

    public Headers getHeaders()
    {
        return headers;
    }

    public void setHeaders(Headers headers)
    {
        this.headers = headers;
    }

    public Metadata getMetadata()
    {
        return metadata;
    }

    public void setMetadata(Metadata metadata)
    {
        this.metadata = metadata;
    }

    public AppData getAppData()
    {
        return appData;
    }

    public void setAppData(AppData appData)
    {
        this.appData = appData;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public List<AssociatedState> getAssociatedStates()
    {
        return associatedStates;
    }

    public void setAssociatedStates(List<AssociatedState> associatedStates)
    {
        this.associatedStates = associatedStates;
    }

    public static class AssociatedState
    {
        private String contentId;
        private String externalId;
        private String state;

        public String getContentId()
        {
            return contentId;
        }

        public void setContentId(String contentId)
        {
            this.contentId = contentId;
        }

        public String getExternalId()
        {
            return externalId;
        }

        public void setExternalId(String externalId)
        {
            this.externalId = externalId;
        }

        public String getState()
        {
            return state;
        }

        public void setState(String state)
        {
            this.state = state;
        }
    }

    public static class Headers
    {
        private String subject;
        private String fromEmail;
        private String fromName;
        private String replyEmail;

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getFromEmail()
        {
            return fromEmail;
        }

        public void setFromEmail(String fromEmail)
        {
            this.fromEmail = fromEmail;
        }

        public String getFromName()
        {
            return fromName;
        }

        public void setFromName(String fromName)
        {
            this.fromName = fromName;
        }

        public String getReplyEmail()
        {
            return replyEmail;
        }

        public void setReplyEmail(String replyEmail)
        {
            this.replyEmail = replyEmail;
        }
    }

    public static class Metadata
    {
        private String createdBy;
        private Date createdAt;
        private String modifiedBy;
        private Date modifiedAt;

        public String getCreatedBy()
        {
            return createdBy;
        }

        public void setCreatedBy(String createdBy)
        {
            this.createdBy = createdBy;
        }

        public Date getCreatedAt()
        {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getModifiedBy()
        {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy)
        {
            this.modifiedBy = modifiedBy;
        }

        public Date getModifiedAt()
        {
            return modifiedAt;
        }

        public void setModifiedAt(Date modifiedAt)
        {
            this.modifiedAt = modifiedAt;
        }
    }

    public static class AppData
    {
        private String workspaceId;
        private String folderId;

        public String getWorkspaceId()
        {
            return workspaceId;
        }

        public void setWorkspaceId(String workspaceId)
        {
            this.workspaceId = workspaceId;
        }

        public String getFolderId()
        {
            return folderId;
        }

        public void setFolderId(String folderId)
        {
            this.folderId = folderId;
        }
    }

    public static class Data
    {
        private Html html;
        private Text text;

        public Html getHtml()
        {
            return html;
        }

        public void setHtml(Html html)
        {
            this.html = html;
        }

        public Text getText()
        {
            return text;
        }

        public void setText(Text text)
        {
            this.text = text;
        }
    }

    public static class Html
    {
        private String body;

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }
    }

    public static class Text
    {
        private String body;
        private boolean syncFromHtml;

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }

        public boolean isSyncFromHtml()
        {
            return syncFromHtml;
        }

        public void setSyncFromHtml(boolean syncFromHtml)
        {
            this.syncFromHtml = syncFromHtml;
        }
    }
}
