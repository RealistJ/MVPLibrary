package com.jxx.mvplibrary.commonwidget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jxx.mvplibrary.R;

/**
 * 类描述:上拉加载更多的布局
 * 创建人:jxx
 * 创建时间:2017/11/1.
 */
public class MyLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.lmv_layout;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
