package com.jxx.mvplibrary.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 类描述:BaseView
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public interface BaseView {
    /*******内嵌加载*******/
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);

    /**
     * 绑定生命周期
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();
}
