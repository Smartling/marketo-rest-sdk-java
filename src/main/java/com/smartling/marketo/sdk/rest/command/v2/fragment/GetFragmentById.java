package com.smartling.marketo.sdk.rest.command.v2.fragment;

import com.google.common.reflect.TypeToken;
import com.smartling.marketo.sdk.domain.v2.fragment.FragmentV2;

import java.util.List;

public class GetFragmentById extends BaseFragmentGetCommand<List<FragmentV2>>
{
    private final String id;

    public GetFragmentById(String id)
    {
        super(new TypeToken<List<FragmentV2>>() {}.getType());
        this.id = id;
    }

    @Override
    public String getPath()
    {
        return "/asset/v2/fragment/" + id;
    }
}
