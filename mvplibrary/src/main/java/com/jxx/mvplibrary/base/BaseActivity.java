package com.jxx.mvplibrary.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.jxx.mvplibrary.R;
import com.jxx.mvplibrary.baseapp.AppManager;
import com.jxx.mvplibrary.baseapp.BaseApplication;
import com.jxx.mvplibrary.baserx.RxManager;
import com.jxx.mvplibrary.commonutils.util.ScreenUtils;
import com.jxx.mvplibrary.commonwidget.StatusBarCompat;
import com.jxx.mvplibrary.daynightmodeutils.ChangeModeController;
import com.jxx.mvplibrary.injector.components.ApplicationComponent;
import com.jxx.mvplibrary.injector.modules.ActivityModule;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述:Activity基类
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements BaseView {
    @Inject
    protected T mPresenter;

    //用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
    public RxManager mRxManager;
    //配置是否有改变
    private boolean isConfigChange = false;
    private Unbinder mUnbinder;

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isConfigChange = false;//配置是否有改变
        mRxManager = new RxManager();//用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
        //设置layout前配置(昼夜主题,管理Activity栈,无标题,横竖屏,着色状态栏等可以设置)
        doBeforeSetcontentView();
        View inflate = getLayoutInflater().inflate(attachLayoutRes(), null);
        setContentView(inflate);
//        setContentView(attachLayoutRes());
        mUnbinder = ButterKnife.bind(this);
        initInjector();
        //P中维护上下文
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }
        initScreen();
        initViews();
    }

    /**
     * 屏幕适配
     */
    private void initScreen() {
        if (ScreenUtils.isPortrait()) {
            ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        } else {
            ScreenUtils.adaptScreen4HorizontalSlide(this, 360);
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
    }

    /**
     * 获取 ActivityModule
     *
     * @return ActivityModule
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        //设置昼夜主题
        initTheme();
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        SetStatusBarColor();

    }

    /**
     * 设置主题
     */
    private void initTheme() {
        ChangeModeController.setTheme(this, R.style.DayTheme, R.style.NightTheme);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChange = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;

        if (mRxManager != null) {
            mRxManager.clear();
        }
        if (!isConfigChange) {
            AppManager.getAppManager().finishActivity(this);
        }
    }
}
