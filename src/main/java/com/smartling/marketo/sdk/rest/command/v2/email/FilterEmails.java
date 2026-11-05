package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterEmails extends BaseEmailGetCommand<EmailV2Page>
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final Map<String, Object> parameters;

    public FilterEmails(EmailV2Filter filter)
    {
        super(EmailV2Page.class);

        // Collect only the filters that are set, keyed by their wire names
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("workspaceId", encodeQueryValue(filter.getWorkspaceId()));
        putIfSet(params, "folderId", encodeQueryValue(filter.getFolderId()));
        putIfSet(params, "folderType", folderTypeValue(filter.getFolderType()));
        putStatusIfNotEmpty(params, filter.getStatus());
        putIfSet(params, "createdAtStart", format(filter.getCreatedAtStart()));
        putIfSet(params, "createdAtEnd", format(filter.getCreatedAtEnd()));
        putIfSet(params, "modifiedAtStart", format(filter.getModifiedAtStart()));
        putIfSet(params, "modifiedAtEnd", format(filter.getModifiedAtEnd()));
        putIfSet(params, "name", encodeQueryValue(filter.getName()));
        putIfSet(params, "sortKey", encodeQueryValue(filter.getSortKey()));
        putIfSet(params, "sortOrder", filter.getSortOrder() == null ? null : filter.getSortOrder().name());
        putIfSet(params, "isCreatedByMe", asString(filter.getCreatedByMe()));
        putIfSet(params, "isModifiedByMe", asString(filter.getModifiedByMe()));
        putIfSet(params, "templateId", encodeQueryValue(filter.getTemplateId()));
        putIfSet(params, "scriptEngine", encodeQueryValue(filter.getScriptEngine()));
        putIfSet(params, "isValueNonNullable", encodeQueryValue(filter.getValueNonNullable()));
        putIfSet(params, "includeArchived", asString(filter.getIncludeArchived()));
        putIfSet(params, "pageIndex", asString(filter.getPageIndex()));
        putIfSet(params, "pageSize", asString(filter.getPageSize()));
        this.parameters = params;
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/filter";
    }

    @Override
    public Map<String, Object> getParameters()
    {
        return parameters;
    }

    private static void putIfSet(Map<String, Object> params, String key, String value)
    {
        if (value != null)
        {
            params.put(key, value);
        }
    }

    private static void putStatusIfNotEmpty(Map<String, Object> params, List<String> status)
    {
        if (status != null && !status.isEmpty())
        {
            params.put("status", status.stream().map(FilterEmails::encodeQueryValue).collect(Collectors.toList()));
        }
    }

    // Percent-encode every reserved character so the HTTP client sends the value as written
    // (it would otherwise keep existing %XX sequences and read {name} as a URI template)
    private static String encodeQueryValue(String value)
    {
        if (value == null)
        {
            return null;
        }

        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String format(LocalDateTime dateTime)
    {
        return dateTime == null ? null : DATE_FORMAT.format(dateTime);
    }

    private static String asString(Object value)
    {
        return value == null ? null : value.toString();
    }

    private static String folderTypeValue(FolderType folderType)
    {
        if (folderType == null)
        {
            return null;
        }

        switch (folderType)
        {
            case FOLDER:
                return "Folder";
            case PROGRAM:
                return "Program";
            default:
                throw new IllegalArgumentException("Unsupported folder type: " + folderType);
        }
    }
}
