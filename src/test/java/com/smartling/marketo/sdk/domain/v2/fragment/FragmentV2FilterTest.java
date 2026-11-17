package com.smartling.marketo.sdk.domain.v2.fragment;

import com.smartling.marketo.sdk.domain.folder.FolderType;
import com.smartling.marketo.sdk.domain.v2.email.SortOrder;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FragmentV2FilterTest
{
    private static final String WORKSPACE_ID = "workspace-id-1";
    private static final String FOLDER_ID = "folder-id-1";
    private static final String FRAGMENT_NAME = "fragment-1";
    private static final String FRAGMENT_TYPE = "visual";
    private static final String SORT_KEY = "name";
    private static final String SCRIPT_ENGINE = "VELOCITY";
    private static final String VALUE_NON_NULLABLE = "true";
    private static final String DRAFT = "draft";
    private static final String PUBLISHED = "published";
    private static final Instant CREATED_AT_START = Instant.parse("2026-01-02T03:04:05Z");
    private static final Instant CREATED_AT_END = Instant.parse("2026-01-03T03:04:05Z");
    private static final Instant MODIFIED_AT_START = Instant.parse("2026-01-04T03:04:05Z");
    private static final Instant MODIFIED_AT_END = Instant.parse("2026-01-05T03:04:05Z");

    @Test
    public void shouldRequireNonBlankWorkspaceId()
    {
        for (String blank : new String[] {null, "", "  "})
        {
            assertThatThrownBy(() -> FragmentV2Filter.builder(blank))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("workspaceId");
        }
    }

    @Test
    public void shouldRejectNullStatusElement()
    {
        FragmentV2Filter.Builder builder = FragmentV2Filter.builder(WORKSPACE_ID);

        assertThatThrownBy(() -> builder.status(Arrays.asList(DRAFT, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("status");
    }

    @Test
    public void shouldCopyStatusList()
    {
        List<String> statuses = new ArrayList<>(Arrays.asList(DRAFT, PUBLISHED));
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID).status(statuses).build();

        statuses.add("other");

        assertThat(filter.getStatus()).containsExactly(DRAFT, PUBLISHED);
        assertThatThrownBy(() -> filter.getStatus().add("x"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldAllowEmptyStatusList()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID).status(new ArrayList<>()).build();

        assertThat(filter.getStatus()).isEmpty();
    }

    @Test
    public void shouldTreatNullStatusAsNotSet()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID).status(null).build();

        assertThat(filter.getStatus()).isNull();
    }

    @Test
    public void shouldDefaultOptionalFiltersToNull()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID).build();

        assertThat(filter.getWorkspaceId()).isEqualTo(WORKSPACE_ID);
        assertThat(filter.getFolderId()).isNull();
        assertThat(filter.getFolderType()).isNull();
        assertThat(filter.getStatus()).isNull();
        assertThat(filter.getCreatedAtStart()).isNull();
        assertThat(filter.getCreatedAtEnd()).isNull();
        assertThat(filter.getModifiedAtStart()).isNull();
        assertThat(filter.getModifiedAtEnd()).isNull();
        assertThat(filter.getName()).isNull();
        assertThat(filter.getFragmentType()).isNull();
        assertThat(filter.getSortKey()).isNull();
        assertThat(filter.getSortOrder()).isNull();
        assertThat(filter.getCreatedByMe()).isNull();
        assertThat(filter.getModifiedByMe()).isNull();
        assertThat(filter.getIncludeArchived()).isNull();
        assertThat(filter.getScriptEngine()).isNull();
        assertThat(filter.getValueNonNullable()).isNull();
        assertThat(filter.getPageIndex()).isNull();
        assertThat(filter.getPageSize()).isNull();
    }

    // Per-getter assertions: FragmentV2Filter has no equals, so the whole object cannot be compared
    @Test
    public void shouldReturnEverythingTheBuilderSet()
    {
        FragmentV2Filter filter = FragmentV2Filter.builder(WORKSPACE_ID)
                .folderId(FOLDER_ID)
                .folderType(FolderType.PROGRAM)
                .status(Arrays.asList(DRAFT, PUBLISHED))
                .createdAtStart(CREATED_AT_START)
                .createdAtEnd(CREATED_AT_END)
                .modifiedAtStart(MODIFIED_AT_START)
                .modifiedAtEnd(MODIFIED_AT_END)
                .name(FRAGMENT_NAME)
                .fragmentType(FRAGMENT_TYPE)
                .sortKey(SORT_KEY)
                .sortOrder(SortOrder.DESC)
                .createdByMe(true)
                .modifiedByMe(false)
                .includeArchived(true)
                .scriptEngine(SCRIPT_ENGINE)
                .valueNonNullable(VALUE_NON_NULLABLE)
                .pageIndex(2)
                .pageSize(25)
                .build();

        assertThat(filter.getWorkspaceId()).isEqualTo(WORKSPACE_ID);
        assertThat(filter.getFolderId()).isEqualTo(FOLDER_ID);
        assertThat(filter.getFolderType()).isEqualTo(FolderType.PROGRAM);
        assertThat(filter.getStatus()).containsExactly(DRAFT, PUBLISHED);
        assertThat(filter.getCreatedAtStart()).isEqualTo(CREATED_AT_START);
        assertThat(filter.getCreatedAtEnd()).isEqualTo(CREATED_AT_END);
        assertThat(filter.getModifiedAtStart()).isEqualTo(MODIFIED_AT_START);
        assertThat(filter.getModifiedAtEnd()).isEqualTo(MODIFIED_AT_END);
        assertThat(filter.getName()).isEqualTo(FRAGMENT_NAME);
        assertThat(filter.getFragmentType()).isEqualTo(FRAGMENT_TYPE);
        assertThat(filter.getSortKey()).isEqualTo(SORT_KEY);
        assertThat(filter.getSortOrder()).isEqualTo(SortOrder.DESC);
        assertThat(filter.getCreatedByMe()).isTrue();
        assertThat(filter.getModifiedByMe()).isFalse();
        assertThat(filter.getIncludeArchived()).isTrue();
        assertThat(filter.getScriptEngine()).isEqualTo(SCRIPT_ENGINE);
        assertThat(filter.getValueNonNullable()).isEqualTo(VALUE_NON_NULLABLE);
        assertThat(filter.getPageIndex()).isEqualTo(2);
        assertThat(filter.getPageSize()).isEqualTo(25);
    }
}
