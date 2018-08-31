package com.jxx.mvplibrary.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 类描述:PerActivity
 * 创建人:jxx
 * 创建时间:2018/8/9.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
