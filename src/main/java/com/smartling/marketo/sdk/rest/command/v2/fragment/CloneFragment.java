package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CloneFragment extends BaseFragmentPostCommand<List<FragmentV2>>
{
    public CloneFragment(String id, String name)
    {
        super(new TypeToken<List<FragmentV2>>() {}.getType(), body(id, name));
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/fragment/clone";
    }

    private static Map<String, Object> body(String id, String name)
    {
        // The copy gets only the new name
        Map<String, Object> newAsset = new LinkedHashMap<>();
        newAsset.put("name", name);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("assetId", id);
        body.put("newAsset", newAsset);

        return body;
    }
}
