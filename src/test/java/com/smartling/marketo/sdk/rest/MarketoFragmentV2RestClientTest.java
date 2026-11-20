package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;
import com.smartling.marketo.sdk.rest.command.v2.fragment.CloneFragment;
import com.smartling.marketo.sdk.rest.command.v2.fragment.FilterFragments;
import com.smartling.marketo.sdk.rest.command.v2.fragment.GetFragmentById;
import com.smartling.marketo.sdk.rest.command.v2.fragment.TransitionFragmentState;
import com.smartling.marketo.sdk.rest.command.v2.fragment.UpdateFragment;
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
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MarketoFragmentV2RestClientTest
{
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String FRAGMENT_ID = "0123456789abcdef01234567";
    private static final String FRAGMENT_PATH_PREFIX = "/asset/v2/fragment/";
    private static final String FILTER_WORD_ID = "filter";
    private static final String AJO_PREFIXED_ID = "ajo:" + FRAGMENT_ID;
    private static final String MISSING_FRAGMENT_ERROR_CODE = "603";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";
    private static final String INVALID_PARAMETER_ERROR_CODE = "611";
    private static final String INVALID_PARAMETER_MESSAGE = "bad parameter";
    private static final String NOT_FOUND_MESSAGE = "Fragment[id = " + FRAGMENT_ID + "] not found";
    private static final String COPY_NAME = "test-fragment-copy";
    private static final String COPY_ID = "copy123";
    private static final String DRAFT_ID = "draft123";
    private static final String CONTENT_REJECTED_ERROR_CODE = "60019";
    private static final String DRAFT_EXISTS_ERROR_CODE = "60000";
    private static final String DELETED_DRAFT_ERROR_CODE = "60051";
    private static final String CLONE_NO_FRAGMENT_MESSAGE = "Clone of fragment[id = " + FRAGMENT_ID + "] returned no fragment";
    private static final String DRAFT_NO_FRAGMENT_MESSAGE = "Create draft of fragment[id = " + FRAGMENT_ID + "] returned no fragment";
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
        FragmentV2Page filteredPage = testedInstance.filterFragments(emptyFilter());

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
        FragmentV2Page filteredPage = testedInstance.filterFragments(emptyFilter());

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
        MarketoApiException accessDeniedError = new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE);
        given(executor.execute(any(FilterFragments.class))).willThrow(accessDeniedError);

        // Filter fragments by workspace
        Throwable thrown = catchThrowable(() -> testedInstance.filterFragments(emptyFilter()));

        assertThat(thrown)
                .isSameAs(accessDeniedError)
                .isExactlyInstanceOf(MarketoApiException.class);
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
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + FRAGMENT_ID);
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
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + FILTER_WORD_ID);
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
                .hasMessage(ACCESS_DENIED_MESSAGE)
                .asInstanceOf(type(MarketoApiException.class))
                .extracting(MarketoApiException::getErrorCode)
                .isEqualTo(MISSING_FRAGMENT_ERROR_CODE);
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

    @Test
    public void shouldCloneFragmentAndReturnFirstFragment() throws Exception
    {
        FragmentV2 firstFragment = fragmentWithId(COPY_ID);
        given(executor.execute(any(CloneFragment.class))).willReturn(Arrays.asList(firstFragment, fragmentWithId("second-fragment")));

        // Clone the fragment under a new name
        FragmentV2 clonedFragment = testedInstance.cloneFragment(FRAGMENT_ID, COPY_NAME);

        ArgumentCaptor<CloneFragment> commandCaptor = ArgumentCaptor.forClass(CloneFragment.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(clonedFragment).isSameAs(firstFragment);
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + "clone");
        assertThat(commandCaptor.getValue().getBody()).isEqualTo(new CloneFragment(FRAGMENT_ID, COPY_NAME).getBody());
    }

    @Test
    public void shouldUpdateFragmentWithUpdateCommand() throws Exception
    {
        FragmentV2Update update = FragmentV2Update.builder().htmlBody("<p>test-html</p>").build();

        // Update the fragment
        testedInstance.updateFragment(FRAGMENT_ID, update);

        ArgumentCaptor<UpdateFragment> commandCaptor = ArgumentCaptor.forClass(UpdateFragment.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + FRAGMENT_ID + "/update");
        assertThat(commandCaptor.getValue().getBody()).isEqualTo(new UpdateFragment(FRAGMENT_ID, update).getBody());
    }

    @Test
    public void shouldCreateDraftAndReturnFirstFragment() throws Exception
    {
        FragmentV2 firstFragment = fragmentWithId(DRAFT_ID);
        given(executor.execute(any(TransitionFragmentState.class))).willReturn(Arrays.asList(firstFragment, fragmentWithId("second-fragment")));

        // Create a draft of the fragment
        FragmentV2 draft = testedInstance.createDraft(FRAGMENT_ID);

        ArgumentCaptor<TransitionFragmentState> commandCaptor = ArgumentCaptor.forClass(TransitionFragmentState.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(draft).isSameAs(firstFragment);
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + "state/transition");
        assertThat(commandCaptor.getValue().getBody()).isEqualTo(TransitionFragmentState.createDraft(FRAGMENT_ID).getBody());
    }

    @Test
    public void shouldApproveFragmentWithTransitionCommand() throws Exception
    {
        // Approve the fragment
        testedInstance.approveFragment(FRAGMENT_ID);

        ArgumentCaptor<TransitionFragmentState> commandCaptor = ArgumentCaptor.forClass(TransitionFragmentState.class);
        verify(executor).execute(commandCaptor.capture());
        assertThat(commandCaptor.getValue().getPath()).isEqualTo(FRAGMENT_PATH_PREFIX + "state/transition");
        assertThat(commandCaptor.getValue().getBody()).isEqualTo(TransitionFragmentState.approve(FRAGMENT_ID).getBody());
    }

    @Test
    public void shouldRejectUnsafeIdsOnEveryWriteMethod()
    {
        List<String> unsafeIds = Arrays.asList(null, "", "  ", "1/2", "{a}", AJO_PREFIXED_ID);

        for (String unsafeId : unsafeIds)
        {
            for (Call call : writeCalls(unsafeId))
            {
                assertThatThrownBy(call::run)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("id must be an alphanumeric token, but was: " + unsafeId);
            }
        }

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldRejectNullNameOnClone()
    {
        assertThatThrownBy(() -> testedInstance.cloneFragment(FRAGMENT_ID, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldRejectNullUpdateOnUpdate()
    {
        assertThatThrownBy(() -> testedInstance.updateFragment(FRAGMENT_ID, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("update must not be null");

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldCheckIdBeforeNameAndUpdate()
    {
        assertThatThrownBy(() -> testedInstance.cloneFragment(null, null)).isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> testedInstance.updateFragment(null, null)).isExactlyInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldThrowObjectNotFoundWhenCloneReturnsNoFragment() throws Exception
    {
        for (List<FragmentV2> noFragmentResult : noFragmentResults())
        {
            given(executor.execute(any(CloneFragment.class))).willReturn(noFragmentResult);

            // Clone the fragment under a new name
            Throwable thrown = catchThrowable(() -> testedInstance.cloneFragment(FRAGMENT_ID, COPY_NAME));

            assertThat(thrown).isExactlyInstanceOf(ObjectNotFoundException.class).hasMessage(CLONE_NO_FRAGMENT_MESSAGE);
        }
    }

    @Test
    public void shouldThrowObjectNotFoundWhenCreateDraftReturnsNoFragment() throws Exception
    {
        for (List<FragmentV2> noFragmentResult : noFragmentResults())
        {
            given(executor.execute(any(TransitionFragmentState.class))).willReturn(noFragmentResult);

            // Create a draft of the fragment
            Throwable thrown = catchThrowable(() -> testedInstance.createDraft(FRAGMENT_ID));

            assertThat(thrown).isExactlyInstanceOf(ObjectNotFoundException.class).hasMessage(DRAFT_NO_FRAGMENT_MESSAGE);
        }
    }

    @Test
    public void shouldMapErrorCode603ToObjectNotFoundOnEveryWriteMethod() throws Exception
    {
        given(executor.execute(any(CloneFragment.class)))
                .willThrow(new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE));
        given(executor.execute(any(UpdateFragment.class)))
                .willThrow(new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE));
        given(executor.execute(any(TransitionFragmentState.class)))
                .willThrow(new MarketoApiException(MISSING_FRAGMENT_ERROR_CODE, ACCESS_DENIED_MESSAGE));

        for (Call call : writeCalls(FRAGMENT_ID))
        {
            Throwable thrown = catchThrowable(call::run);

            assertThat(thrown)
                    .isExactlyInstanceOf(ObjectNotFoundException.class)
                    .hasMessage(ACCESS_DENIED_MESSAGE)
                    .asInstanceOf(type(MarketoApiException.class))
                    .extracting(MarketoApiException::getErrorCode)
                    .isEqualTo(MISSING_FRAGMENT_ERROR_CODE);
        }
    }

    @Test
    public void shouldPassContentRejectedThroughUnchanged() throws Exception
    {
        ContentRejectedException contentRejectedError = new ContentRejectedException(CONTENT_REJECTED_ERROR_CODE, "rejected");
        given(executor.execute(any(UpdateFragment.class))).willThrow(contentRejectedError);

        // Update the fragment
        Throwable thrown = catchThrowable(() -> testedInstance.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody("<p>test-html</p>").build()));

        assertThat(thrown).isSameAs(contentRejectedError);
    }

    @Test
    public void shouldPropagateExistingDraftErrorUnchanged() throws Exception
    {
        MarketoApiException existingDraftError = new MarketoApiException(DRAFT_EXISTS_ERROR_CODE, "draft exists");
        given(executor.execute(any(TransitionFragmentState.class))).willThrow(existingDraftError);

        // Create a draft of the fragment
        Throwable thrown = catchThrowable(() -> testedInstance.createDraft(FRAGMENT_ID));

        assertThat(thrown)
                .isSameAs(existingDraftError)
                .isExactlyInstanceOf(MarketoApiException.class);
    }

    @Test
    public void shouldPropagateDeletedDraftErrorUnchanged() throws Exception
    {
        MarketoApiException deletedDraftError = new MarketoApiException(DELETED_DRAFT_ERROR_CODE, "draft deleted");
        given(executor.execute(any(UpdateFragment.class))).willThrow(deletedDraftError);

        // Update the fragment
        Throwable thrown = catchThrowable(() -> testedInstance.updateFragment(FRAGMENT_ID, FragmentV2Update.builder().htmlBody("<p>test-html</p>").build()));

        assertThat(thrown)
                .isSameAs(deletedDraftError)
                .isExactlyInstanceOf(MarketoApiException.class);
    }

    private List<Call> writeCalls(String id)
    {
        return Arrays.asList(
                () -> testedInstance.cloneFragment(id, COPY_NAME),
                () -> testedInstance.updateFragment(id, FragmentV2Update.builder().htmlBody("<p>test-html</p>").build()),
                () -> testedInstance.createDraft(id),
                () -> testedInstance.approveFragment(id));
    }

    private static List<List<FragmentV2>> noFragmentResults()
    {
        return Arrays.asList(null, Collections.<FragmentV2>emptyList(), Collections.singletonList(new FragmentV2()));
    }

    private static FragmentV2Filter emptyFilter()
    {
        return FragmentV2Filter.builder(WORKSPACE_ID)
                .build();
    }

    private static FragmentV2 fragmentWithId(String id)
    {
        FragmentV2 fragment = new FragmentV2();
        fragment.setId(id);
        return fragment;
    }

    private interface Call
    {
        void run() throws Exception;
    }
}
