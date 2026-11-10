package com.smartling.marketo.sdk.rest.command.v2.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.rest.transport.ObjectMapperProvider;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterEmails extends BaseEmailGetCommand<EmailV2Page>
{
    // The API reads a zone-less date-time as UTC
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapperProvider().getContext(null);

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

        // Expose the parameters as a read-only view
        this.parameters = Collections.unmodifiableMap(params);
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/filter";
    }

    /**
     * The values of the string filters and of each {@code status} element are already percent-encoded, because the
     * HTTP client keeps existing {@code %XX} sequences as written and reads {@code {...}} as a URI template. They
     * therefore also appear encoded in the error messages the executor builds from these parameters.
     */
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
            params.put("status", Collections.unmodifiableList(status.stream().map(FilterEmails::encodeQueryValue).collect(Collectors.toList())));
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

    private static String format(Instant instant)
    {
        return instant == null ? null : DATE_FORMAT.format(instant);
    }

    private static String asString(Object value)
    {
        return value == null ? null : value.toString();
    }

    // The executor sends query parameters with toString() (FOLDER), so take the wire name from the
    // enum's @JsonProperty (Folder) through the SDK's mapper instead of repeating it here
    private static String folderTypeValue(FolderType folderType)
    {
        return folderType == null ? null : OBJECT_MAPPER.convertValue(folderType, String.class);
    }
}
