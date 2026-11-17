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
    private static final String FOLDER_ID = "folder-1";
    private static final String FRAGMENT_NAME = "fragment-1";
    private static final String FRAGMENT_TYPE = "fragment-type-1";
    private static final String SORT_KEY = "test-sort-key";
    private static final String SCRIPT_ENGINE = "script-engine-1";
    private static final String VALUE_NON_NULLABLE = "value-non-nullable-1";
    private static final String SPECIAL_VALUE = "a+b&c=d#e %41 {x} é";
    private static final String ENCODED_SPECIAL_VALUE = "a%2Bb%26c%3Dd%23e%20%2541%20%7Bx%7D%20%C3%A9";
    private static final List<String> STATUSES = Arrays.asList("draft", "approved");

    private static final Instant CREATED_AT_START = Instant.parse("2026-10-28T08:27:00Z");
    private static final String CREATED_AT_START_WIRE = "2026-10-28T08:27:00";
    private static final Instant MIDNIGHT = Instant.parse("2026-10-28T00:00:00Z");
    private static final String MIDNIGHT_WIRE = "2026-10-28T00:00:00";

    private static final String WORKSPACE_ID_KEY = "workspaceId";
    private static final String FOLDER_TYPE_KEY = "folderType";
    private static final String SORT_ORDER_KEY = "sortOrder";
    private static final String PAGE_INDEX_KEY = "pageIndex";
    private static final String INCLUDE_ARCHIVED_KEY = "includeArchived";
    private static final String CREATED_AT_START_KEY = "createdAtStart";
    private static final String CREATED_AT_END_KEY = "createdAtEnd";
    private static final String MODIFIED_AT_START_KEY = "modifiedAtStart";
    private static final String MODIFIED_AT_END_KEY = "modifiedAtEnd";
    private static final String STATUS_KEY = "status";
    private static final String NAME_KEY = "name";
    private static final String FRAGMENT_TYPE_KEY = "fragmentType";
    private static final String CREATED_BY_ME_KEY = "isCreatedByMe";
    private static final String MODIFIED_BY_ME_KEY = "isModifiedByMe";

    @Test
    public void shouldTargetFilterEndpoint()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .build());

        assertThat(command.getPath()).isEqualTo("/asset/v2/fragment/filter");
        assertThat(command.getMethod()).isEqualTo("GET");
        assertThat(command.getHeaders()).isEqualTo(Collections.singletonMap("x-app-type", "marketo"));
        assertThat(command.getResultType()).isEqualTo(FragmentV2Page.class);
    }

    @Test
    public void shouldPutOnlyWorkspaceIdForMinimalFilter()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .build());

        assertThat(command.getParameters()).isEqualTo(Collections.singletonMap(WORKSPACE_ID_KEY, WORKSPACE_ID));
    }

    @Test
    public void shouldConvertEveryFilterToItsWireValue()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .folderId(FOLDER_ID)
                .folderType(FolderType.FOLDER)
                .status(STATUSES)
                .createdAtStart(CREATED_AT_START)
                .createdAtEnd(Instant.parse("2026-10-29T09:28:00Z"))
                .modifiedAtStart(Instant.parse("2026-10-30T10:29:00Z"))
                .modifiedAtEnd(Instant.parse("2026-10-31T11:30:00Z"))
                .name(FRAGMENT_NAME)
                .fragmentType(FRAGMENT_TYPE)
                .sortKey(SORT_KEY)
                .sortOrder(SortOrder.ASC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
                .scriptEngine(SCRIPT_ENGINE)
                .valueNonNullable(VALUE_NON_NULLABLE)
                .pageIndex(0)
                .pageSize(50)
                .build();

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put("folderId", FOLDER_ID);
        expected.put(FOLDER_TYPE_KEY, "Folder");
        expected.put(STATUS_KEY, STATUSES);
        expected.put(CREATED_AT_START_KEY, CREATED_AT_START_WIRE);
        expected.put(CREATED_AT_END_KEY, "2026-10-29T09:28:00");
        expected.put(MODIFIED_AT_START_KEY, "2026-10-30T10:29:00");
        expected.put(MODIFIED_AT_END_KEY, "2026-10-31T11:30:00");
        expected.put(NAME_KEY, FRAGMENT_NAME);
        expected.put(FRAGMENT_TYPE_KEY, FRAGMENT_TYPE);
        expected.put("sortKey", SORT_KEY);
        expected.put(SORT_ORDER_KEY, "ASC");
        expected.put(CREATED_BY_ME_KEY, "true");
        expected.put(MODIFIED_BY_ME_KEY, "false");
        expected.put("scriptEngine", SCRIPT_ENGINE);
        expected.put("isValueNonNullable", VALUE_NON_NULLABLE);
        expected.put(INCLUDE_ARCHIVED_KEY, "true");
        expected.put(PAGE_INDEX_KEY, "0");
        expected.put("pageSize", "50");

        // Build the command from the filter
        FilterFragments command = new FilterFragments(filter);

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldPercentEncodeStringFilters()
    {
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put(STATUS_KEY, Collections.singletonList(ENCODED_SPECIAL_VALUE));
        expected.put(NAME_KEY, ENCODED_SPECIAL_VALUE);
        expected.put(FRAGMENT_TYPE_KEY, ENCODED_SPECIAL_VALUE);

        // Build the command from a filter whose strings need percent-encoding
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .name(SPECIAL_VALUE)
                .fragmentType(SPECIAL_VALUE)
                .status(Collections.singletonList(SPECIAL_VALUE))
                .build());

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
        FilterFragments folderCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .folderType(FolderType.FOLDER)
                .build());
        FilterFragments programCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .folderType(FolderType.PROGRAM)
                .build());

        assertThat(folderCommand.getParameters()).isEqualTo(expectedParameters(FOLDER_TYPE_KEY, "Folder"));
        assertThat(programCommand.getParameters()).isEqualTo(expectedParameters(FOLDER_TYPE_KEY, "Program"));
    }

    @Test
    public void shouldConvertSortOrders()
    {
        FilterFragments ascendingCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .sortOrder(SortOrder.ASC)
                .build());
        FilterFragments descendingCommand = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .sortOrder(SortOrder.DESC)
                .build());

        assertThat(ascendingCommand.getParameters()).isEqualTo(expectedParameters(SORT_ORDER_KEY, "ASC"));
        assertThat(descendingCommand.getParameters()).isEqualTo(expectedParameters(SORT_ORDER_KEY, "DESC"));
    }

    @Test
    public void shouldSendZeroPageIndexAndFalseBoolean()
    {
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put(PAGE_INDEX_KEY, "0");
        expected.put(INCLUDE_ARCHIVED_KEY, "false");

        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .pageIndex(0)
                .includeArchived(false)
                .build());

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    @Test
    public void shouldOmitEmptyStatusList()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .status(Collections.<String>emptyList())
                .build());

        assertThat(command.getParameters()).isEqualTo(Collections.singletonMap(WORKSPACE_ID_KEY, WORKSPACE_ID));
    }

    @Test
    public void shouldSendZeroSecondsOfDates()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtStart(CREATED_AT_START)
                .build());

        assertThat(command.getParameters()).isEqualTo(expectedParameters(CREATED_AT_START_KEY, CREATED_AT_START_WIRE));
    }

    @Test
    public void shouldDropFractionalSecondsFromDates()
    {
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtEnd(Instant.parse("2026-10-28T08:27:44.5Z"))
                .build());

        assertThat(command.getParameters()).isEqualTo(expectedParameters(CREATED_AT_END_KEY, "2026-10-28T08:27:44"));
    }

    @Test
    public void shouldSendDatesInUtc()
    {
        Instant instantWithPlusTwoOffset = OffsetDateTime.parse("2026-10-28T10:27:00+02:00").toInstant();

        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .modifiedAtEnd(instantWithPlusTwoOffset)
                .build());

        assertThat(command.getParameters()).isEqualTo(expectedParameters(MODIFIED_AT_END_KEY, "2026-10-28T08:27:00"));
    }

    @Test
    public void shouldNeverSendDateOnlyValues()
    {
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put(CREATED_AT_START_KEY, MIDNIGHT_WIRE);
        expected.put(CREATED_AT_END_KEY, MIDNIGHT_WIRE);
        expected.put(MODIFIED_AT_START_KEY, MIDNIGHT_WIRE);
        expected.put(MODIFIED_AT_END_KEY, MIDNIGHT_WIRE);

        // Build the command from a filter whose four dates fall on midnight
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdAtStart(MIDNIGHT)
                .createdAtEnd(MIDNIGHT)
                .modifiedAtStart(MIDNIGHT)
                .modifiedAtEnd(MIDNIGHT)
                .build());

        assertThat(command.getParameters()).isEqualTo(expected);
    }

    private static Map<String, Object> expectedParameters(String parameterName, Object parameterValue)
    {
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put(parameterName, parameterValue);
        return expected;
    }

    private static void assertBooleanParameters(boolean createdByMe, boolean modifiedByMe, boolean includeArchived)
    {
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put(WORKSPACE_ID_KEY, WORKSPACE_ID);
        expected.put(CREATED_BY_ME_KEY, String.valueOf(createdByMe));
        expected.put(MODIFIED_BY_ME_KEY, String.valueOf(modifiedByMe));
        expected.put(INCLUDE_ARCHIVED_KEY, String.valueOf(includeArchived));

        // Build the command from a filter with the three boolean flags
        FilterFragments command = new FilterFragments(FragmentV2Filter.builder(WORKSPACE_ID)
                .createdByMe(createdByMe)
                .modifiedByMe(modifiedByMe)
                .includeArchived(includeArchived)
                .build());

        assertThat(command.getParameters()).isEqualTo(expected);
    }
}
