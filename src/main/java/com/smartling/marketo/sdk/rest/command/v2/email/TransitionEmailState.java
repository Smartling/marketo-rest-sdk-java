package com.smartling.marketo.sdk.rest.command.v2.email;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2StateAction;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransitionEmailState<R> extends BaseEmailPostCommand<R>
{
    private TransitionEmailState(Type resultType, String id, EmailV2StateAction action)
    {
        super(resultType, body(id, action));
    }

    /**
     * Approves the draft; any result the API sends is ignored.
     */
    public static TransitionEmailState<Void> approve(String id)
    {
        return new TransitionEmailState<>(Void.TYPE, id, EmailV2StateAction.APPROVE);
    }

    /**
     * Creates a draft of the published email; the API answers with the email.
     */
    public static TransitionEmailState<List<EmailV2>> createDraft(String id)
    {
        return new TransitionEmailState<>(new TypeToken<List<EmailV2>>() {}.getType(), id, EmailV2StateAction.CREATE_DRAFT);
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/email/state/transition";
    }

    private static Map<String, Object> body(String id, EmailV2StateAction action)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("contentId", id);
        body.put("action", action.getApiValue());

        return body;
    }
}
