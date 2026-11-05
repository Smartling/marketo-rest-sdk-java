package com.smartling.marketo.sdk.domain.v2.email;

import com.smartling.marketo.sdk.domain.folder.FolderType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable filter for the Asset API v2 email list endpoint. Every property except {@code workspaceId} is optional;
 * an unset property is {@code null} and is not sent.
 */
public final class EmailV2Filter
{
    private final String workspaceId;
    private final String folderId;
    private final FolderType folderType;
    private final List<String> status;
    private final LocalDateTime createdAtStart;
    private final LocalDateTime createdAtEnd;
    private final LocalDateTime modifiedAtStart;
    private final LocalDateTime modifiedAtEnd;
    private final String name;
    private final String sortKey;
    private final SortOrder sortOrder;
    private final Boolean createdByMe;
    private final Boolean modifiedByMe;
    private final Boolean includeArchived;
    private final String templateId;
    private final String scriptEngine;
    private final String valueNonNullable;
    private final Integer pageIndex;
    private final Integer pageSize;

    private EmailV2Filter(Builder builder)
    {
        this.workspaceId = builder.workspaceId;
        this.folderId = builder.folderId;
        this.folderType = builder.folderType;
        this.status = builder.status;
        this.createdAtStart = builder.createdAtStart;
        this.createdAtEnd = builder.createdAtEnd;
        this.modifiedAtStart = builder.modifiedAtStart;
        this.modifiedAtEnd = builder.modifiedAtEnd;
        this.name = builder.name;
        this.sortKey = builder.sortKey;
        this.sortOrder = builder.sortOrder;
        this.createdByMe = builder.createdByMe;
        this.modifiedByMe = builder.modifiedByMe;
        this.includeArchived = builder.includeArchived;
        this.templateId = builder.templateId;
        this.scriptEngine = builder.scriptEngine;
        this.valueNonNullable = builder.valueNonNullable;
        this.pageIndex = builder.pageIndex;
        this.pageSize = builder.pageSize;
    }

    public static Builder builder(String workspaceId)
    {
        return new Builder(workspaceId);
    }

    public String getWorkspaceId()
    {
        return workspaceId;
    }

    public String getFolderId()
    {
        return folderId;
    }

    public FolderType getFolderType()
    {
        return folderType;
    }

    public List<String> getStatus()
    {
        return status;
    }

    public LocalDateTime getCreatedAtStart()
    {
        return createdAtStart;
    }

    public LocalDateTime getCreatedAtEnd()
    {
        return createdAtEnd;
    }

    public LocalDateTime getModifiedAtStart()
    {
        return modifiedAtStart;
    }

    public LocalDateTime getModifiedAtEnd()
    {
        return modifiedAtEnd;
    }

    public String getName()
    {
        return name;
    }

    public String getSortKey()
    {
        return sortKey;
    }

    public SortOrder getSortOrder()
    {
        return sortOrder;
    }

    public Boolean getCreatedByMe()
    {
        return createdByMe;
    }

    public Boolean getModifiedByMe()
    {
        return modifiedByMe;
    }

    public Boolean getIncludeArchived()
    {
        return includeArchived;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public String getScriptEngine()
    {
        return scriptEngine;
    }

    public String getValueNonNullable()
    {
        return valueNonNullable;
    }

    public Integer getPageIndex()
    {
        return pageIndex;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public static final class Builder
    {
        private final String workspaceId;
        private String folderId;
        private FolderType folderType;
        private List<String> status;
        private LocalDateTime createdAtStart;
        private LocalDateTime createdAtEnd;
        private LocalDateTime modifiedAtStart;
        private LocalDateTime modifiedAtEnd;
        private String name;
        private String sortKey;
        private SortOrder sortOrder;
        private Boolean createdByMe;
        private Boolean modifiedByMe;
        private Boolean includeArchived;
        private String templateId;
        private String scriptEngine;
        private String valueNonNullable;
        private Integer pageIndex;
        private Integer pageSize;

        private Builder(String workspaceId)
        {
            if (workspaceId == null || workspaceId.trim().isEmpty())
            {
                throw new IllegalArgumentException("workspaceId must not be blank");
            }
            this.workspaceId = workspaceId;
        }

        public Builder folderId(String folderId)
        {
            this.folderId = folderId;
            return this;
        }

        public Builder folderType(FolderType folderType)
        {
            this.folderType = folderType;
            return this;
        }

        /**
         * Sets the statuses to match; {@code null} means "not set". A null element is rejected.
         */
        public Builder status(List<String> status)
        {
            if (status == null)
            {
                this.status = null;
                return this;
            }

            if (status.stream().anyMatch(Objects::isNull))
            {
                throw new IllegalArgumentException("status must not contain null elements");
            }
            this.status = Collections.unmodifiableList(new ArrayList<>(status));
            return this;
        }

        public Builder createdAtStart(LocalDateTime createdAtStart)
        {
            this.createdAtStart = createdAtStart;
            return this;
        }

        public Builder createdAtEnd(LocalDateTime createdAtEnd)
        {
            this.createdAtEnd = createdAtEnd;
            return this;
        }

        public Builder modifiedAtStart(LocalDateTime modifiedAtStart)
        {
            this.modifiedAtStart = modifiedAtStart;
            return this;
        }

        public Builder modifiedAtEnd(LocalDateTime modifiedAtEnd)
        {
            this.modifiedAtEnd = modifiedAtEnd;
            return this;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder sortKey(String sortKey)
        {
            this.sortKey = sortKey;
            return this;
        }

        public Builder sortOrder(SortOrder sortOrder)
        {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder createdByMe(Boolean createdByMe)
        {
            this.createdByMe = createdByMe;
            return this;
        }

        public Builder modifiedByMe(Boolean modifiedByMe)
        {
            this.modifiedByMe = modifiedByMe;
            return this;
        }

        public Builder includeArchived(Boolean includeArchived)
        {
            this.includeArchived = includeArchived;
            return this;
        }

        public Builder templateId(String templateId)
        {
            this.templateId = templateId;
            return this;
        }

        public Builder scriptEngine(String scriptEngine)
        {
            this.scriptEngine = scriptEngine;
            return this;
        }

        public Builder valueNonNullable(String valueNonNullable)
        {
            this.valueNonNullable = valueNonNullable;
            return this;
        }

        public Builder pageIndex(Integer pageIndex)
        {
            this.pageIndex = pageIndex;
            return this;
        }

        public Builder pageSize(Integer pageSize)
        {
            this.pageSize = pageSize;
            return this;
        }

        public EmailV2Filter build()
        {
            return new EmailV2Filter(this);
        }
    }
}
