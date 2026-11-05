package com.smartling.marketo.sdk.rest.command.v2.email;

import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterEmailsTest
{
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String SPECIAL_VALUE = "a+b&c=d#e %41 {x} é";
    private static final String ENCODED_SPECIAL_VALUE = "a%2Bb%26c%3Dd%23e%20%2541%20%7Bx%7D%20%C3%A9";
    private static final List<String> STATUSES = Arrays.asList("draft", "approved");

    @Test
    public void shouldTargetFilterEndpoint()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID).build());

        assertThat(command.getPath()).isEqualTo("/asset/v2/email/filter");
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getResultType()).isEqualTo(EmailV2Page.class);
    }

    @Test
    public void shouldPutOnlyWorkspaceIdForMinimalFilter()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID).build());

        assertThat(command.getParameters()).isEqualTo(Collections.singletonMap("workspaceId", WORKSPACE_ID));
    }

    @Test
    public void shouldConvertEveryFilterToItsWireValue()
    {
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID)
                .folderId("folder-1")
                .folderType(FolderType.FOLDER)
                .status(STATUSES)
                .createdAtStart(LocalDateTime.of(2026, 10, 28, 8, 27))
                .createdAtEnd(LocalDateTime.of(2026, 10, 29, 9, 28))
                .modifiedAtStart(LocalDateTime.of(2026, 10, 30, 10, 29))
                .modifiedAtEnd(LocalDateTime.of(2026, 10, 31, 11, 30))
                .name("test-name")
                .sortKey("test-sort-key")
                .sortOrder(SortOrder.ASC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
                .templateId("template-1")
                .scriptEngine("script-engine-1")
                .valueNonNullable("value-non-nullable-1")
                .pageIndex(0)
                .pageSize(50)
                .build();
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("workspaceId", WORKSPACE_ID);
        expected.put("folderId", "folder-1");
        expected.put("folderType", "Folder");
        expected.put("status", STATUSES);
        expected.put("createdAtStart", "2026-10-28T08:27:00");
        expected.put("createdAtEnd", "2026-10-29T09:28:00");
        expected.put("modifiedAtStart", "2026-10-30T10:29:00");
        expected.put("modifiedAtEnd", "2026-10-31T11:30:00");
        expected.put("name", "test-name");
        expected.put("sortKey", "test-sort-key");
        expected.put("sortOrder", "ASC");
        expected.put("isCreatedByMe", "true");
        expected.put("isModifiedByMe", "false");
        expected.put("templateId", "template-1");
        expected.put("scriptEngine", "script-engine-1");
        expected.put("isValueNonNullable", "value-non-nullable-1");
        expected.put("includeArchived", "true");
        expected.put("pageIndex", "0");
        expected.put("pageSize", "50");

        FilterEmails command = new FilterEmails(filter);

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldPercentEncodeStringFilters()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID)
                .name(SPECIAL_VALUE)
                .status(Collections.singletonList(SPECIAL_VALUE))
                .build());
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("workspaceId", WORKSPACE_ID);
        expected.put("status", Collections.singletonList(ENCODED_SPECIAL_VALUE));
        expected.put("name", ENCODED_SPECIAL_VALUE);

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldConvertBooleanFiltersIndependently()
    {
        assertBooleanParameters(false, true, false);
        assertBooleanParameters(true, false, true);
    }

    @Test
    public void shouldConvertProgramFolderTypeToProgram()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID).folderType(FolderType.PROGRAM).build());

        assertThat(command.getParameters().get("folderType")).isEqualTo("Program");
    }

    @Test
    public void shouldSendZeroPageIndexAndFalseBoolean()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID)
                .pageIndex(0)
                .includeArchived(false)
                .build());

        assertThat(command.getParameters().get("pageIndex")).isEqualTo("0");
        assertThat(command.getParameters().get("includeArchived")).isEqualTo("false");
    }

    @Test
    public void shouldOmitEmptyStatusList()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID)
                .status(Collections.<String>emptyList())
                .build());

        assertThat(command.getParameters()).doesNotContainKey("status");
    }

    @Test
    public void shouldDropNanosecondsFromDates()
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID)
                .createdAtStart(LocalDateTime.of(2026, 10, 28, 8, 27, 30, 123_000_000))
                .build());

        assertThat(command.getParameters().get("createdAtStart")).isEqualTo("2026-10-28T08:27:30");
    }

    private static void assertBooleanParameters(boolean createdByMe, boolean modifiedByMe, boolean includeArchived)
    {
        FilterEmails command = new FilterEmails(EmailV2Filter.builder(WORKSPACE_ID)
                .createdByMe(createdByMe)
                .modifiedByMe(modifiedByMe)
                .includeArchived(includeArchived)
                .build());
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("workspaceId", WORKSPACE_ID);
        expected.put("isCreatedByMe", String.valueOf(createdByMe));
        expected.put("isModifiedByMe", String.valueOf(modifiedByMe));
        expected.put("includeArchived", String.valueOf(includeArchived));

        assertThat(command.getParameters()).isEqualTo(expected);
    }
}
