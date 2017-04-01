package com.gavin.emojiinputfilter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情过滤器
 *
 * @author gavin.xiong 2016/12/21
 */
public class EmojiInputFilter implements InputFilter {

    private Context context;

    private String[] faceImages;
    private String[] faceScr;

    private Pattern pattern;

    private int emojiDpSize;

    private ArrayMap<String, Integer> faceBook;

    public EmojiInputFilter(Context context) {
        this.context = context;
        faceImages = context.getResources().getStringArray(R.array.emoji);
        faceScr = context.getResources().getStringArray(R.array.emoji_desc);

        pattern = buildPattern();
        faceBook = buildEmojiToRes();

        float scale = Resources.getSystem().getDisplayMetrics().density;
        emojiDpSize = (int) (20 * scale + 0.5f);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();

        // 新输入的字符串为空（删除剪切等）
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }

        return addEmojiSpans(sourceText);
    }

    private CharSequence addEmojiSpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int resId = faceBook.get(matcher.group());
            Drawable dr = ContextCompat.getDrawable(context, resId);
            dr.setBounds(0, 0, emojiDpSize, emojiDpSize);
            builder.setSpan(new ImageSpan(dr, ImageSpan.ALIGN_BOTTOM), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(faceScr.length * 3);
        patternString.append('(');
        for (String s : faceScr) {
            patternString.append(Pattern.quote(s)).append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    private ArrayMap<String, Integer> buildEmojiToRes() {
        if (faceImages.length != faceScr.length) {
            throw new IllegalStateException("Emoji resource ID/text mismatch");
        }
        ArrayMap<String, Integer> smileyToRes = new ArrayMap<>(faceScr.length);
        String packageName = context.getPackageName();
        for (int i = 0; i < faceScr.length; i++) {
            int resId = context.getResources().getIdentifier(faceImages[i], "drawable", packageName);
            smileyToRes.put(faceScr[i], resId);
        }
        return smileyToRes;
    }
}