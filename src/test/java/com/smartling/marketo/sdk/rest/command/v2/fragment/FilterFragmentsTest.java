package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterFragmentsTest
{
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String SPECIAL_VALUE = "a+b&c=d#e %41 {x} é";
    private static final String ENCODED_SPECIAL_VALUE = "a%2Bb%26c%3Dd%23e%20%2541%20%7Bx%7D%20%C3%A9";
    private static final List<String> STATUSES = Arrays.asList("draft", "approved");
    private static final String DATE_TIME_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}";

    @Test
    public void shouldTargetFilterEndpoint()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).build());

        assertThat(command.getPath()).isEqualTo("/asset/v2/fragment/filter");
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getResultType()).isEqualTo(FragmentV2Page.class);
    }

    @Test
    public void shouldPutOnlyWorkspaceIdForMinimalFilter()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).build());

        assertThat(command.getParameters()).isEqualTo(Collections.singletonMap("workspaceId", WORKSPACE_ID));
    }

    @Test
    public void shouldConvertEveryFilterToItsWireValue()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .folderId("folder-1")
                .folderType(FolderType.FOLDER)
                .status(STATUSES)
                .createdAtStart(Instant.parse("2026-10-28T08:27:00Z"))
                .createdAtEnd(Instant.parse("2026-10-29T09:28:00Z"))
                .modifiedAtStart(Instant.parse("2026-10-30T10:29:00Z"))
                .modifiedAtEnd(Instant.parse("2026-10-31T11:30:00Z"))
                .name("fragment-1")
                .fragmentType("fragment-type-1")
                .sortKey("test-sort-key")
                .sortOrder(SortOrder.ASC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
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
        expected.put("name", "fragment-1");
        expected.put("fragmentType", "fragment-type-1");
        expected.put("sortKey", "test-sort-key");
        expected.put("sortOrder", "ASC");
        expected.put("isCreatedByMe", "true");
        expected.put("isModifiedByMe", "false");
        expected.put("scriptEngine", "script-engine-1");
        expected.put("isValueNonNullable", "value-non-nullable-1");
        expected.put("includeArchived", "true");
        expected.put("pageIndex", "0");
        expected.put("pageSize", "50");

        // Build the command from the filter
        FilterFragments command = new FilterFragments(filter);

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldPercentEncodeStringFilters()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .name(SPECIAL_VALUE)
                .fragmentType(SPECIAL_VALUE)
                .status(Collections.singletonList(SPECIAL_VALUE))
                .build());
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("workspaceId", WORKSPACE_ID);
        expected.put("status", Collections.singletonList(ENCODED_SPECIAL_VALUE));
        expected.put("name", ENCODED_SPECIAL_VALUE);
        expected.put("fragmentType", ENCODED_SPECIAL_VALUE);

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldConvertBooleanFiltersIndependently()
    {
        assertBooleanParameters(false, true, false);
        assertBooleanParameters(true, false, true);
    }

    @Test
    public void shouldConvertFolderTypes()
    {
        FilterFragments folderCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).folderType(FolderType.FOLDER).build());
        FilterFragments programCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).folderType(FolderType.PROGRAM).build());

        assertThat(folderCommand.getParameters().get("folderType")).isEqualTo("Folder");
        assertThat(programCommand.getParameters().get("folderType")).isEqualTo("Program");
    }

    @Test
    public void shouldConvertSortOrders()
    {
        FilterFragments ascendingCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).sortOrder(SortOrder.ASC).build());
        FilterFragments descendingCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID).sortOrder(SortOrder.DESC).build());

        assertThat(ascendingCommand.getParameters().get("sortOrder")).isEqualTo("ASC");
        assertThat(descendingCommand.getParameters().get("sortOrder")).isEqualTo("DESC");
    }

    @Test
    public void shouldSendZeroPageIndexAndFalseBoolean()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .pageIndex(0)
                .includeArchived(false)
                .build());

        assertThat(command.getParameters().get("pageIndex")).isEqualTo("0");
        assertThat(command.getParameters().get("includeArchived")).isEqualTo("false");
    }

    @Test
    public void shouldOmitEmptyStatusList()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .status(Collections.<String>emptyList())
                .build());

        assertThat(command.getParameters()).doesNotContainKey("status");
    }

    @Test
    public void shouldSendZeroSecondsOfDates()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtStart(Instant.parse("2026-10-28T08:27:00Z"))
                .build());

        assertThat(command.getParameters().get("createdAtStart")).isEqualTo("2026-10-28T08:27:00");
    }

    @Test
    public void shouldDropFractionalSecondsFromDates()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtEnd(Instant.parse("2026-10-28T08:27:44.5Z"))
                .build());

        assertThat(command.getParameters().get("createdAtEnd")).isEqualTo("2026-10-28T08:27:44");
    }

    @Test
    public void shouldSendDatesInUtc()
    {
        Instant instantWithPlusTwoOffset = OffsetDateTime.parse("2026-10-28T10:27:00+02:00").toInstant();

        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .modifiedAtEnd(instantWithPlusTwoOffset)
                .build());

        assertThat(command.getParameters().get("modifiedAtEnd")).isEqualTo("2026-10-28T08:27:00");
    }

    @Test
    public void shouldNeverSendDateOnlyValues()
    {
        Instant midnight = Instant.parse("2026-10-28T00:00:00Z");

        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtStart(midnight)
                .createdAtEnd(midnight)
                .modifiedAtStart(midnight)
                .modifiedAtEnd(midnight)
                .build());

        assertThat(command.getParameters().get("createdAtStart")).asString().matches(DATE_TIME_PATTERN);
        assertThat(command.getParameters().get("createdAtEnd")).asString().matches(DATE_TIME_PATTERN);
        assertThat(command.getParameters().get("modifiedAtStart")).asString().matches(DATE_TIME_PATTERN);
        assertThat(command.getParameters().get("modifiedAtEnd")).asString().matches(DATE_TIME_PATTERN);
    }

    private static void assertBooleanParameters(boolean createdByMe, boolean modifiedByMe, boolean includeArchived)
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
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
