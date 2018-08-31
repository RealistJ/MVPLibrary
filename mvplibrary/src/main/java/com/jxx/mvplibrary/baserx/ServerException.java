package com.jxx.mvplibrary.baserx;


import com.jxx.mvplibrary.baseapp.AppConfig;

/**
 * 类描述:服务器请求异常
 * 创建人:jxx
 * 创建时间:2018/8/1.
 */
public class ServerException extends RuntimeException {
    private int mErrorCode;

    public ServerException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == AppConfig.TOKEN_EXPRIED;
    }
}
