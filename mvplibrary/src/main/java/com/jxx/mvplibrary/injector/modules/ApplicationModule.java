package com.jxx.mvplibrary.injector.modules;

import android.content.Context;

import com.jxx.mvplibrary.baseapp.BaseApplication;
import com.jxx.mvplibrary.baserx.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 类描述:ApplicationModule
 * 创建人:jxx
 * 创建时间:2018/8/8.
 */
@Module
public class ApplicationModule {

    private final BaseApplication mApplication;
    private final RxBus mRxBus;

    public ApplicationModule(BaseApplication application, RxBus rxBus) {
        mApplication = application;
        mRxBus = rxBus;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return mRxBus;
    }
}
