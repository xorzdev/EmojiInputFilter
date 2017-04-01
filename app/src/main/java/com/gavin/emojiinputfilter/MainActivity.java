package com.gavin.emojiinputfilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private RecyclerView recyclerFace;

    private String[] emojiNames, emojiImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerFace = (RecyclerView) findViewById(R.id.recycler);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        InputFilter[] inputFilters = new InputFilter[]{new EmojiInputFilter(this)};
        textView.setFilters(inputFilters);
        editText.setFilters(inputFilters);

        initEmoji();


        editText.setText("@心里有鬼丶丶丶");
    }

    /**
     * 初始化表情
     */
    private void initEmoji() {
        emojiNames = getResources().getStringArray(R.array.emoji_desc);
        emojiImages = getResources().getStringArray(R.array.emoji);

        recyclerFace.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false));
        final List<Emoji> stringList = new ArrayList<>();
        String packageName = getPackageName();
        for (int i = 0; i < emojiNames.length; i++) {
            int resId = getResources().getIdentifier(emojiImages[i], "drawable", packageName);
            stringList.add(new Emoji(resId, emojiNames[i]));
        }
        BindingAdapter adapter = new BindingAdapter<>(this, stringList, R.layout.item_face, com.gavin.emojiinputfilter.BR.item);
        adapter.setOnItemClickListener(new BindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Editable editable = editText.getText();
                int selection = editText.getSelectionStart() + emojiNames[position].length();
                String temp = editable.subSequence(0, editText.getSelectionStart())
                        + emojiNames[position]
                        + editText.getText().subSequence(editText.getSelectionEnd(), editable.length());
                editText.setText(temp);
                editText.setSelection(selection);
            }
        });
        recyclerFace.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(editText.getText());
            }
        });
    }
}
