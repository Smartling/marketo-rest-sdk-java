package com.smartling.marketo.sdk.domain.v2.email;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmailV2FilterTest
{
    private static final String WORKSPACE_ID = "3";
    private static final String DRAFT = "draft";
    private static final String APPROVED = "approved";

    @Test
    public void shouldRequireNonBlankWorkspaceId()
    {
        for (String blank : new String[] {null, "", "  "})
        {
            assertThatThrownBy(() -> EmailV2Filter.builder(blank))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("workspaceId");
        }
    }

    @Test
    public void shouldRejectNullStatusElement()
    {
        EmailV2Filter.Builder builder = EmailV2Filter.builder(WORKSPACE_ID);

        assertThatThrownBy(() -> builder.status(Arrays.asList(DRAFT, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("status");
    }

    @Test
    public void shouldAcceptImmutableStatusList()
    {
        List<String> statuses = List.of(DRAFT, APPROVED);

        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID).status(statuses).build();

        assertThat(filter.getStatus()).isEqualTo(statuses);
    }

    @Test
    public void shouldCopyStatusList()
    {
        List<String> statuses = new ArrayList<>(Arrays.asList(DRAFT, APPROVED));
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID).status(statuses).build();

        statuses.add("other");

        assertThat(filter.getStatus()).containsExactly(DRAFT, APPROVED);
        assertThatThrownBy(() -> filter.getStatus().add("x"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void shouldTreatNullStatusAsNotSet()
    {
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID).status(null).build();

        assertThat(filter.getStatus()).isNull();
    }

    @Test
    public void shouldDefaultOptionalFiltersToNull()
    {
        EmailV2Filter filter = EmailV2Filter.builder(WORKSPACE_ID).build();

        assertThat(filter.getWorkspaceId()).isEqualTo(WORKSPACE_ID);
        assertThat(filter.getFolderId()).isNull();
        assertThat(filter.getFolderType()).isNull();
        assertThat(filter.getStatus()).isNull();
        assertThat(filter.getCreatedAtStart()).isNull();
        assertThat(filter.getCreatedAtEnd()).isNull();
        assertThat(filter.getModifiedAtStart()).isNull();
        assertThat(filter.getModifiedAtEnd()).isNull();
        assertThat(filter.getName()).isNull();
        assertThat(filter.getSortKey()).isNull();
        assertThat(filter.getSortOrder()).isNull();
        assertThat(filter.getCreatedByMe()).isNull();
        assertThat(filter.getModifiedByMe()).isNull();
        assertThat(filter.getIncludeArchived()).isNull();
        assertThat(filter.getTemplateId()).isNull();
        assertThat(filter.getScriptEngine()).isNull();
        assertThat(filter.getValueNonNullable()).isNull();
        assertThat(filter.getPageIndex()).isNull();
        assertThat(filter.getPageSize()).isNull();
    }
}
