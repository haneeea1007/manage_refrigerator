package com.example.refrigeproject.show_foods;

import androidx.annotation.LayoutRes;

import com.saber.stickyheader.stickyData.HeaderData;


public class HeaderDataInfo implements HeaderData {
    public static final int HEADER_TYPE = 1;

    private int headerType;

    @LayoutRes
    private final int layoutResource;

    public HeaderDataInfo(int headerType, @LayoutRes int layoutResource) {
        this.layoutResource = layoutResource;
        this.headerType = headerType;
    }

    @LayoutRes
    @Override
    public int getHeaderLayout() {
        //return layout of yourHeader
        return layoutResource;
    }

    @Override
    public int getHeaderType() {
        return headerType;
    }
}
