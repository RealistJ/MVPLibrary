package com.jxx.mvplibrary.baserx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述:RxJava调度管理
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class RxSchedulers {
    /**
     * subscribeOn:在IO线程线程
     * observeOn:在主线程观察
     * subscribeOn 作用于该操作符之前的 Observable 的创建操符作以及 doOnSubscribe 操作符 ，换句话说就是 doOnSubscribe 以及 Observable 的创建操作符总是被其之后最近的 subscribeOn 控制
     * observeOn作用于该操作符之后操作符直到出现新的observeOn操作符
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
