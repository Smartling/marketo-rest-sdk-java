package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import com.smartling.marketo.sdk.rest.command.v2.fragment.FilterFragments;
import com.smartling.marketo.sdk.rest.command.v2.fragment.GetFragmentById;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MarketoFragmentV2RestClientTest
{
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";
    private static final String FILTER_WORD_ID = "filter";
    private static final String AJO_PREFIXED_ID = "ajo:" + FRAGMENT_ID;
    private static final String MISSING_FRAGMENT_ERROR_CODE = "603";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";
    private static final String INVALID_PARAMETER_ERROR_CODE = "611";
    private static final String INVALID_PARAMETER_MESSAGE = "bad parameter";
    private static final String NOT_FOUND_MESSAGE = "Fragment[id = " + FRAGMENT_ID + "] not found";
    private static final String NO_ID_MESSAGE = "Fragment[id = " + FRAGMENT_ID + "] returned without an id";

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoFragmentV2RestClient testedInstance;

    @Test
    public void shouldExecuteFilterCommandAndReturnPage() throws Exception
    {
        FragmentV2Page page = new FragmentV2Page();
        given(executor.execute(any(FilterFragments.class))).willReturn(page);

        // Filter fragments by workspace
        FragmentV2Page filteredPage = testedInstance.filterFragments(FragmentV2Filter.builder(WORKSPACE_ID).build());

        ArgumentCaptor<FilterFragments> commandCaptor = ArgumentCaptor.forClass(FilterFragments.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(filteredPage).isSameAs(page);
        assertThat(commandCaptor.getValue().getPath()).isEqualTo("/asset/v2/fragment/filter");
        assertThat(commandCaptor.getValue().getParameters()).isEqualTo(Collections.singletonMap("workspaceId", WORKSPACE_ID));
    }

    @Test
    public void shouldReturnEmptyPageWhenFilterResultIsNull() throws Exception
    {
        given(executor.execute(any(FilterFragments.class))).willReturn(null);

        // Filter fragments by workspace
        FragmentV2Page filteredPage = testedInstance.filterFragments(FragmentV2Filter.builder(WORKSPACE_ID).build());

        assertThat(filteredPage).usingRecursiveComparison().isEqualTo(FragmentV2Page.empty());
    }

    @Test
    public void shouldRejectNullFilter()
    {
        assertThatThrownBy(() -> testedInstance.filterFragments(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("filter must not be null");

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldLeaveMissingFragmentErrorOnFilterAsApiException() throws Exception
    {
        MarketoApiException missingWorkspaceError = new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE);
        given(executor.execute(any(FilterFragments.class))).willThrow(missingWorkspaceError);

        // Filter fragments by workspace
        Throwable thrown = catchThrowable(() -> testedInstance.filterFragments(FragmentV2Filter.builder(WORKSPACE_ID).build()));

        assertThat(thrown).isSameAs(missingWorkspaceError);
        assertThat(thrown).isExactlyInstanceOf(MarketoApiException.class);
    }

    @Test
    public void shouldReturnFirstFragmentForLoad() throws Exception
    {
        FragmentV2 firstFragment = fragmentWithId(FRAGMENT_ID);
        FragmentV2 secondFragment = fragmentWithId("second-fragment");
        given(executor.execute(any(GetFragmentById.class))).willReturn(Arrays.asList(firstFragment, secondFragment));

        // Load the fragment by id
        FragmentV2 loadedFragment = testedInstance.loadFragmentById(FRAGMENT_ID);

        ArgumentCaptor<GetFragmentById> commandCaptor = ArgumentCaptor.forClass(GetFragmentById.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(loadedFragment).isSameAs(firstFragment);
        assertThat(commandCaptor.getValue().getPath()).isEqualTo("/asset/v2/fragment/" + FRAGMENT_ID);
    }

    @Test
    public void shouldLetFilterWordThroughAsId() throws Exception
    {
        FragmentV2 fragment = fragmentWithId(FILTER_WORD_ID);
        given(executor.execute(any(GetFragmentById.class))).willReturn(Collections.singletonList(fragment));

        // Load the fragment whose id is the word "filter"
        FragmentV2 loadedFragment = testedInstance.loadFragmentById(FILTER_WORD_ID);

        ArgumentCaptor<GetFragmentById> commandCaptor = ArgumentCaptor.forClass(GetFragmentById.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(loadedFragment).isSameAs(fragment);
        assertThat(commandCaptor.getValue().getPath()).isEqualTo("/asset/v2/fragment/" + FILTER_WORD_ID);
    }

    @Test
    public void shouldThrowObjectNotFoundForEmptyResult() throws Exception
    {
        given(executor.execute(any(GetFragmentById.class))).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> testedInstance.loadFragmentById(FRAGMENT_ID))
                .isExactlyInstanceOf(ObjectNotFoundException.class)
                .hasMessage(NOT_FOUND_MESSAGE);
    }

    @Test
    public void shouldThrowObjectNotFoundForNullResult() throws Exception
    {
        given(executor.execute(any(GetFragmentById.class))).willReturn(null);

        assertThatThrownBy(() -> testedInstance.loadFragmentById(FRAGMENT_ID))
                .isExactlyInstanceOf(ObjectNotFoundException.class)
                .hasMessage(NOT_FOUND_MESSAGE);
    }

    @Test
    public void shouldThrowObjectNotFoundForFirstFragmentWithoutId() throws Exception
    {
        given(executor.execute(any(GetFragmentById.class))).willReturn(Collections.singletonList(new FragmentV2()));

        assertThatThrownBy(() -> testedInstance.loadFragmentById(FRAGMENT_ID))
                .isExactlyInstanceOf(ObjectNotFoundException.class)
                .hasMessage(NO_ID_MESSAGE);
    }

    @Test
    public void shouldMapErrorCode603ToObjectNotFound() throws Exception
    {
        given(executor.execute(any(GetFragmentById.class)))
                .willThrow(new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE));

        // Load the fragment by id
        Throwable thrown = catchThrowable(() -> testedInstance.loadFragmentById(FRAGMENT_ID));

        assertThat(thrown)
                .isExactlyInstanceOf(ObjectNotFoundException.class)
                .hasMessage(ACCESS_DENIED_MESSAGE);
        assertThat(((ObjectNotFoundException) thrown).getErrorCode()).isEqualTo(MISSING_FRAGMENT_ERROR_CODE);
    }

    @Test
    public void shouldRethrowOtherApiErrorsUnchanged() throws Exception
    {
        MarketoApiException invalidParameterError = new MarketoApiException(INVALID_PARAMETER_ERROR_CODE, INVALID_PARAMETER_MESSAGE);
        given(executor.execute(any(GetFragmentById.class))).willThrow(invalidParameterError);

        assertThatThrownBy(() -> testedInstance.loadFragmentById(FRAGMENT_ID)).isSameAs(invalidParameterError);
    }

    @Test
    public void shouldRejectUnsafeIds()
    {
        List<String> unsafeIds = Arrays.asList(
                null,
                "",
                " ",
                AJO_PREFIXED_ID,
                "1/2",
                "{a}",
                "a?b",
                " 1234",
                "1234\n",
                "+1234",
                "-1");

        for (String unsafeId : unsafeIds)
        {
            assertThatThrownBy(() -> testedInstance.loadFragmentById(unsafeId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("id must be an alphanumeric token, but was: " + unsafeId);
        }

        verifyNoInteractions(executor);
    }

    private static FragmentV2 fragmentWithId(String id)
    {
        FragmentV2 fragment = new FragmentV2();
        fragment.setId(id);
        return fragment;
    }
}
