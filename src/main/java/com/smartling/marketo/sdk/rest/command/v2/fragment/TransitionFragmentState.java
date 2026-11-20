package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2StateAction;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransitionFragmentState<R> extends BaseFragmentPostCommand<R>
{
    private TransitionFragmentState(Type resultType, String id, FragmentV2StateAction action)
    {
        super(resultType, body(id, action));
    }

    /**
     * Approves the draft; any result the API sends is ignored.
     */
    public static TransitionFragmentState<Void> approve(String id)
    {
        return new TransitionFragmentState<>(Void.TYPE, id, FragmentV2StateAction.APPROVE);
    }

    /**
     * Creates a draft of the published fragment; the API answers with the draft.
     */
    public static TransitionFragmentState<List<FragmentV2>> createDraft(String id)
    {
        return new TransitionFragmentState<>(
                new TypeToken<List<FragmentV2>>() {}.getType(),
                id,
                FragmentV2StateAction.CREATE_DRAFT);
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/fragment/state/transition";
    }

    private static Map<String, Object> body(String id, FragmentV2StateAction action)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("contentId", id);
        body.put("action", action.getApiValue());

        return body;
    }
}
