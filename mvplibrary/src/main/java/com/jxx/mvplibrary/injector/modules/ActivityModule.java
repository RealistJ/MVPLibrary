package com.jxx.mvplibrary.injector.modules;

import android.app.Activity;

import com.jxx.mvplibrary.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * 类描述:Activity Module
 * 创建人:jxx
 * 创建时间:2018/8/9.
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @PerActivity
    @Provides
    Activity getActivity() {
        return mActivity;
    }
}
