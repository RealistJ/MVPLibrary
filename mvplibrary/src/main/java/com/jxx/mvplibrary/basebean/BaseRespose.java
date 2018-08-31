package com.jxx.mvplibrary.basebean;

import java.io.Serializable;

/**
 * des:服务器返回数据的基类
 */
public class BaseRespose <T> implements Serializable {
    public String code;
    public String msg;

    public T data;

    public boolean success() {
        return "0".equals(code);
    }

    @Override
    public String toString() {
        return "BaseRespose{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
