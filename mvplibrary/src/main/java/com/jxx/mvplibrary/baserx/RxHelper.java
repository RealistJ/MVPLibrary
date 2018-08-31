package com.jxx.mvplibrary.baserx;


/**************使用例子******************/
/*_apiService.login(mobile, verifyCode)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .//省略*/

import com.jxx.mvplibrary.basebean.BaseRespose;
import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述:对服务器返回数据成功和失败处理
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class RxHelper {
    /**
     * 对服务器返回数据进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseRespose<T>, T> handleResult() {
        return new ObservableTransformer<BaseRespose<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseRespose<T>> upstream) {
                return upstream.flatMap(new Function<BaseRespose<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(BaseRespose<T> result) throws Exception {
                        Logger.d("result from api : " + result);
                        if (result.success()) {
                            //请求成功,创建成功的数据
                            return createData(result.data);
                        } else {
                            //请求失败
                            return Observable.error(new ServerException(Integer.parseInt(result.code), result.msg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(data);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
