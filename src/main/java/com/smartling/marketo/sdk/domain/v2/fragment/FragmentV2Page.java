package com.smartling.marketo.sdk.domain.v2.fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentV2Page
{
    private List<FragmentV2> items = new ArrayList<>();
    private int totalItems;
    private int pageSize;
    private int currentPage;

    public static FragmentV2Page empty()
    {
        FragmentV2Page page = new FragmentV2Page();
        page.setItems(new ArrayList<>());
        return page;
    }

    public List<FragmentV2> getItems()
    {
        // Jackson writes the field directly, so an explicit null can still arrive here
        if (items == null)
        {
            items = new ArrayList<>();
        }

        return items;
    }

    public void setItems(List<FragmentV2> items)
    {
        this.items = items == null ? new ArrayList<>() : items;
    }

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems(int totalItems)
    {
        this.totalItems = totalItems;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }
}
