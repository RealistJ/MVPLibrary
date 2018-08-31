package com.jxx.mvplibrary.baserx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 类描述:用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class RxManager {
    public RxBus mRxBus = RxBus.getInstance();
    //管理rxbus订阅,存放被观察者对象
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    /**
     * 管理Observables 和 Subscribers订阅
     */
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * RxBus注入监听
     *
     * @param eventName 监听事件的名字
     * @param consumer  观察者
     */
    public <T> void on(String eventName, Consumer<T> consumer) {
        //注册一个被监听者
        Observable<T> mObservable = mRxBus.register(eventName);
        //将被监听者放入集合
        mObservables.put(eventName, mObservable);
        /** 订阅管理 添加一个Subscription subscribe方法(Action1<? super T> onNext,Action1<Throwable> onError)*/
        mCompositeDisposable.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     *
     * @param disposable
     */
    public void add(Disposable disposable) {
        /*订阅管理*/
        mCompositeDisposable.add(disposable);
    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {
        //注意:CompositeDisposable的clear()方法和dispose()方法类似，clear()可以多次被调用来丢弃容器中所有的Disposable，但dispose()被调用一次后就会失效
//        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();// 取消所有订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察
        }
    }

    //发送rxbus
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
