package com.jxx.mvplibrary.baserx;

import android.content.Context;

import com.jxx.mvplibrary.commonutils.ACache;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

//################################使用例子#############################
/*
Observable<LoginData> fromNetwork = Api.getDefault()
        .login(phone, password)
        .compose(RxHelper.handleResult());//对服务器返回数据成功和失败的预处理

        RxCache.load(context,cacheKey,1000*60*30,fromNetwork,false)
        .subscribe(new RxSubscribe<LoginData>(context, "登录中...") {
@Override
protected void _onNext(LoginData data) {
        showToast(R.string.login_success);
        //TODO login success
        }

@Override
protected void _onError(String message) {
        showToast(message);
        }
        });

        compose()是唯一一个能从流中获取原生Observable 的方法，因此，影响整个流的操作符（像subscribeOn()和observeOn()）需要使用compose()，相对的，如果你在flatMap()中使用subscribeOn()/observeOn()，它只影响你创建的flatMap()中的Observable,而不是整个流。
        当你创建一个Observable流并且内联了一堆操作符以后，compose()会立即执行，flatMap()则是在onNext()被调用以后才会执行，换句话说，flatMap()转换的是每个项目，而compose()转换的是整个流。
        flatMap()一定是低效率的，因为他每次调用onNext()之后都需要创建一个新的Observable，compose()是操作在整个流
 */

/**
 * 类描述:处理服务器数据的缓存
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class RxCache {
    /**
     * 先从本地缓存中获取被观察者对象,如果有(而且没过期)就用缓存的数据
     * 没有缓存的话就请求网络
     *
     * @param context      上下文
     * @param cacheKey     缓存内容的Key
     * @param expireTime   有效时间
     * @param fromNetwork  被观察者对象
     * @param forceRefresh 是否强制刷新
     * @param <T>          服务器返回类型的泛型(Bean类)
     * @return 返回被观察者对象
     */
    public static <T> Observable<T> load(final Context context,
                                         final String cacheKey,
                                         final int expireTime,
                                         Observable<T> fromNetwork,
                                         boolean forceRefresh) {

        Observable<T> fromCache = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                //获取缓存
                T cache = (T) ACache.get(context).getAsObject(cacheKey);
                if (cache != null) {
                    emitter.onNext(cache);
                } else {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        /**
         * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
         */
        fromNetwork = fromNetwork.map(new Function<T, T>() {
            @Override
            public T apply(T result) throws Exception {
                //保存缓存
                ACache.get(context).put(cacheKey, (Serializable) result, expireTime);
                return result;
            }
        });
        //强制刷新则返回接口数据
        if (forceRefresh) {
            return fromNetwork;
        } else {
            //两个Observable拼接,优先返回缓存
            // TODO: 2018/8/10 优先返回哪个
            return Observable.concat(fromCache, fromNetwork);
        }
    }
}
