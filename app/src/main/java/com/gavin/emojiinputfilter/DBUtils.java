package com.gavin.emojiinputfilter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class DBUtils {


    @BindingAdapter({"resId"})
    public static void loadIcon(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }
}