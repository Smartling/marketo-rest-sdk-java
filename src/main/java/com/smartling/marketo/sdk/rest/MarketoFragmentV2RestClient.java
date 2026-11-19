package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.MarketoApiException;
import com.smartling.marketo.sdk.MarketoFragmentV2Client;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Filter;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Page;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2Update;
import com.smartling.marketo.sdk.rest.command.v2.fragment.CloneFragment;
import com.smartling.marketo.sdk.rest.command.v2.fragment.FilterFragments;
import com.smartling.marketo.sdk.rest.command.v2.fragment.GetFragmentById;
import com.smartling.marketo.sdk.rest.command.v2.fragment.TransitionFragmentState;
import com.smartling.marketo.sdk.rest.command.v2.fragment.UpdateFragment;

import java.util.List;
import java.util.Objects;

public class MarketoFragmentV2RestClient implements MarketoFragmentV2Client
{
    private static final String MISSING_FRAGMENT_ERROR_CODE = "603";

    private final HttpCommandExecutor httpCommandExecutor;

    MarketoFragmentV2RestClient(HttpCommandExecutor httpCommandExecutor)
    {
        this.httpCommandExecutor = Objects.requireNonNull(httpCommandExecutor, "httpCommandExecutor must not be null");
    }

    @Override
    public FragmentV2Page filterFragments(FragmentV2Filter filter) throws MarketoApiException
    {
        Objects.requireNonNull(filter, "filter must not be null");

        // Filter the fragments; a missing page becomes an empty one
        FragmentV2Page page = httpCommandExecutor.execute(new FilterFragments(filter));

        return page != null ? page : FragmentV2Page.empty();
    }

    @Override
    public FragmentV2 loadFragmentById(String id) throws MarketoApiException
    {
        requireAlphanumericId(id);

        // Load the fragment, reporting Marketo's "missing fragment" error as not found
        List<FragmentV2> fragments = executeReportingMissingFragment(new GetFragmentById(id));

        // A null or empty result array means that no fragment has this id
        if (fragments == null || fragments.isEmpty())
        {
            throw new ObjectNotFoundException(String.format("Fragment[id = %s] not found", id));
        }

        // A fragment without an id cannot be used by the caller
        FragmentV2 fragment = fragments.get(0);
        if (fragment.getId() == null)
        {
            throw new ObjectNotFoundException(String.format("Fragment[id = %s] returned without an id", id));
        }

        return fragment;
    }

    @Override
    public FragmentV2 cloneFragment(String id, String name) throws MarketoApiException
    {
        requireAlphanumericId(id);
        Objects.requireNonNull(name, "name must not be null");

        // Clone the fragment and take the copy from the answer
        List<FragmentV2> fragments = executeReportingMissingFragment(new CloneFragment(id, name));

        return firstFragmentWithId(fragments, String.format("Clone of fragment[id = %s] returned no fragment", id));
    }

    @Override
    public void updateFragment(String id, FragmentV2Update update) throws MarketoApiException
    {
        requireAlphanumericId(id);
        Objects.requireNonNull(update, "update must not be null");

        // Update the fragment; the answer carries nothing the caller needs
        executeReportingMissingFragment(new UpdateFragment(id, update));
    }

    @Override
    public FragmentV2 createDraft(String id) throws MarketoApiException
    {
        requireAlphanumericId(id);

        // Create the draft and take it from the answer
        List<FragmentV2> fragments = executeReportingMissingFragment(TransitionFragmentState.createDraft(id));

        return firstFragmentWithId(fragments, String.format("Create draft of fragment[id = %s] returned no fragment", id));
    }

    @Override
    public void approveFragment(String id) throws MarketoApiException
    {
        requireAlphanumericId(id);

        // Approve the fragment; the answer carries nothing the caller needs
        executeReportingMissingFragment(TransitionFragmentState.approve(id));
    }

    private void requireAlphanumericId(String id)
    {
        // The id goes into the URL path, so only plain alphanumeric tokens are allowed
        if (id == null || !id.matches("[A-Za-z0-9]+"))
        {
            throw new IllegalArgumentException("id must be an alphanumeric token, but was: " + id);
        }
    }

    private <R> R executeReportingMissingFragment(Command<R> command) throws MarketoApiException
    {
        try
        {
            return httpCommandExecutor.execute(command);
        }
        catch (MarketoApiException e)
        {
            // Marketo answers a missing fragment with error 603; report it as not found so that callers do not retry it
            if (MISSING_FRAGMENT_ERROR_CODE.equals(e.getErrorCode()))
            {
                throw new ObjectNotFoundException(e.getErrorCode(), e.getMessage());
            }

            throw e;
        }
    }

    private FragmentV2 firstFragmentWithId(List<FragmentV2> fragments, String noFragmentMessage) throws ObjectNotFoundException
    {
        // A null or empty result, or a first fragment without an id, means the call produced no usable fragment
        if (fragments == null || fragments.isEmpty() || fragments.get(0).getId() == null)
        {
            throw new ObjectNotFoundException(noFragmentMessage);
        }

        return fragments.get(0);
    }
}
