package com.jxx.mvplibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxx.mvplibrary.baseapp.BaseApplication;
import com.jxx.mvplibrary.baserx.RxManager;
import com.jxx.mvplibrary.injector.components.ApplicationComponent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述:Fragment 基类
 * 创建人:jxx
 * 创建时间:2018/8/10.
 */
public abstract class BaseFragment<T extends BasePresenter> extends RxFragment implements BaseView {
    @Inject
    protected T mPresenter;

    protected Context mContext;
    //缓存Fragment view
    private View mRootView;
    private boolean mIsMulti = false;
    public RxManager mRxManager;
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), null);
            mRxManager = new RxManager();
            mUnbinder = ButterKnife.bind(this, mRootView);
            initInjector();
            if(mPresenter!=null){
                mPresenter.mContext=this.getActivity();
            }
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return BaseApplication.getAppComponent();
//        return ((MvpApplication) getActivity().getApplication().get).getAppComponent();
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     *
     * @param isRefresh 新增参数，用来判断是否为下拉刷新调用，下拉刷新的时候不应该再显示加载界面和异常界面
     */
    protected abstract void updateViews(boolean isRefresh);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mRxManager.clear();
    }
}
