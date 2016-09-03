package com.kit.imagelib.interfaces;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Zhao on 16/7/3.
 */
public interface IImageLoader {

    void display(ImageView iv, String url);

    Bitmap loadImageSync(String url);

    File getCacheImage(String url);


    void cancelDisplayTask(ImageView iv);

    void loadImage(String url, @Nullable ImageLoadingListener imageLoadingListener
            , @Nullable ImageLoadingProgressListener imageLoadingProgressListener);


}