package com.jxx.mvplibrary.injector.components;

import android.content.Context;

import com.jxx.mvplibrary.baserx.RxBus;
import com.jxx.mvplibrary.injector.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 类描述:ApplicationComponent
 * 创建人:jxx
 * 创建时间:2018/8/8.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    // provide
    Context getContext();
    RxBus getRxBus();
}
