package com.jxx.mvplibrary.base;

import android.content.Context;

import com.jxx.mvplibrary.baserx.RxManager;


/**
 * 类描述:BasePresenter
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public abstract class BasePresenter<T, E> {
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManage = new RxManager();

    public BasePresenter(T mView, E mModel) {
        this.mModel = mModel;
        this.mView = mView;
        this.onStart();
    }

    public void onStart() {
    }

    public void onDestroy() {
        mRxManage.clear();
    }
}
