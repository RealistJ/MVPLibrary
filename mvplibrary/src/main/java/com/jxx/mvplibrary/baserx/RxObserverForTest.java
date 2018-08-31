package com.jxx.mvplibrary.baserx;

import android.app.Activity;
import android.content.Context;

import com.jxx.mvplibrary.R;
import com.jxx.mvplibrary.baseapp.AppConfig;
import com.jxx.mvplibrary.baseapp.BaseApplication;
import com.jxx.mvplibrary.commonutils.util.NetworkUtils;
import com.jxx.mvplibrary.commonwidget.LoadingDialogGif;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxObserver<User user>(mContext,false) {
@Override
public void _onNext(User user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/

/**
 * 类描述:订阅者基类
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public abstract class RxObserverForTest<T> implements Observer<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog = true;
    }

    public void hideDialog() {
        this.showDialog = true;
    }

    public RxObserverForTest(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxObserverForTest(Context context) {
        this(context, BaseApplication.getAppContext().getString(R.string.loading), true);
    }

    public RxObserverForTest(Context context, boolean showDialog) {
        this(context, BaseApplication.getAppContext().getString(R.string.loading), showDialog);
    }

    @Override
    public void onComplete() {
        if (showDialog) {
            LoadingDialogGif.cancelDialogForLoading();
        }
        _onCompleted();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (showDialog) {
            try {
                LoadingDialogGif.showDialogForLoading((Activity) mContext, msg, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _onSubscribe(d);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (showDialog)
            LoadingDialogGif.cancelDialogForLoading();
        e.printStackTrace();
        //网络
        if (!NetworkUtils.isAvailableByPing()) {
            _onError(BaseApplication.getAppContext().getString(R.string.no_net));
        }
        //服务器
        else if (e instanceof ServerException) {
            ServerException exception = (ServerException) e;
            if (exception.isTokenExpried()) {
                //处理token失效对应的逻辑
                //通知更新
                RxBus.getInstance().post(AppConfig.TOKEN_EXPRIED_STRING, AppConfig.TOKEN_EXPRIED);
            } else {
                _onError(e.getMessage());
            }
        }
        //其它
        else {
            _onError(BaseApplication.getAppContext().getString(R.string.net_error));
        }
    }

    protected abstract void _onSubscribe(Disposable d);

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

    protected abstract void _onCompleted();

    public void test() {
        //1.表示下游不关心任何事件,你上游尽管发你的数据
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe();

        //2.onNext()
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //处理OnNext()
            }
        });

        //3.onNext(),onError()
        Disposable subscribe1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //处理OnNext()
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //处理OnError()
            }
        });

        //onNext(),onError(),onComplete(),
        Disposable subscribe2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //处理OnNext()
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //处理OnError()
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //处理OnComplete()
            }
        });

        //onNext(),onError(),onComplete(),onSubscribe()
        Disposable subscribe3 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //处理OnNext()
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //处理OnError()
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //处理OnComplete()
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                //拿到Disposable,可以用来解绑,取消监听
            }
        });

        //Observer()综合了-->>onNext(),onError(),onComplete(),onSubscribe()
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
