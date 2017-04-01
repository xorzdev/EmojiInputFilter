# EmojiInputFilter
EmojiInputFilter EditText用InputFilter将文字替换成表情

简书地址：http://www.jianshu.com/p/b9212b5a28f6



## 核心代码
```
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
```
## 使用
`
textView.setFilters(new InputFilter[]{new EmojiInputFilter(this)});
`
或
`
editText.setFilters(new InputFilter[]{new EmojiInputFilter(this)});
`

## 上图

![微信图片_20170401155316.jpg](http://upload-images.jianshu.io/upload_images/4336778-17376ecea232d1fa.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 完整代码已上传至github
https://github.com/gavinxxxxxx/EmojiInputFilter
