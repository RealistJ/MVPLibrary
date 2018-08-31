package com.jxx.mvplibrary.baseapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.jxx.mvplibrary.baserx.RxBus;
import com.jxx.mvplibrary.injector.components.ApplicationComponent;
import com.jxx.mvplibrary.injector.components.DaggerApplicationComponent;
import com.jxx.mvplibrary.injector.modules.ApplicationModule;

/**
 * 类描述:application基类
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class BaseApplication extends Application{
    private static Context baseApplication;
    private static ApplicationComponent sAppComponent;
    // 因为下载那边需要用，这里在外面实例化在通过 ApplicationModule 设置
    private RxBus mRxBus = RxBus.getInstance();


    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        initDatabase();
        initInjector();
    }

    public static Context getAppContext() {
        return baseApplication;
    }

    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //onTerminate 当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，那
        // 么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进程
        //使用Application如果保存了一些不该保存的对象很容易导致内存泄漏。如果在Application的oncreate中执行比较 耗时的操作，将直接影响的程序的启动时间。
        // 清理工作不能依靠onTerminate完成，因为android会尽量让你的程序一直运行，所以很有可能 onTerminate不会被调用。
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//分包
    }

    /**
     * 初始化注射器
     */
    private void initInjector() {
        // 这里不做注入操作，只提供一些全局单例数据
        sAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mRxBus))
                .build();
    }

    /**
     * 获取ApplicationComponent
     * @return
     */
    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }
    /**
     * 初始化数据库
     */
    private void initDatabase() {

    }
}
