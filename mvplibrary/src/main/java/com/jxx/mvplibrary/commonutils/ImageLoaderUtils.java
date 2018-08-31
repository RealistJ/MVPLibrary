package com.jxx.mvplibrary.commonutils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jxx.mvplibrary.R;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        //Glide4.0之前
//        Glide.with(context)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//all:缓存源资源和转换后的资源, none:不作任何磁盘缓存,source:缓存源资源,result：缓存转换后的资源
//                .placeholder(placeholder)
//                .error(error)
//                .crossFade()
//                .into(imageView);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)// 正在加载中的图片
                .error(error) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void displayForCIV(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.user_image_default) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .thumbnail(0.5f)//缩略图
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransformUtil(context))
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(url) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

    public static void displayRound(Context context, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new GlideRoundTransformUtil(context))
                .placeholder(R.drawable.ic_image_loading)// 正在加载中的图片
                .error(R.drawable.ic_empty_picture) // 加载失败的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL); // 磁盘缓存策略

        Glide.with(context)
                .load(resId) // 图片地址
                .apply(options) // 参数
                .into(imageView); // 需要显示的ImageView控件
    }

}
