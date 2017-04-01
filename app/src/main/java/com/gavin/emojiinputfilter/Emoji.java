package com.gavin.emojiinputfilter;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/4/1
 */
public class Emoji {

    private int resId;
    private String name;

    public Emoji(int resId, String name) {
        this.resId = resId;
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public String getName() {
        return name;
    }
}
