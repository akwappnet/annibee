package com.devstree.annibee.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class MobileEditText extends EditText implements TextWatcher {


    public MobileEditText(Context context) {
        super(context);
        addTextChangedListener(this);
    }

    public MobileEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    public MobileEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = getString();
        int textLength = length();
        if (text.endsWith("-") || text.endsWith(" "))
            return;
        if (textLength == 1) {
            if (!text.contains("(")) {
                setText(new StringBuilder(text).insert(text.length() - 1, "(").toString());
                setSelection(length());
            }
        } else if (textLength == 5) {
            if (!text.contains(")")) {
                setText(new StringBuilder(text).insert(text.length() - 1, ")").toString());
                setSelection(length());
            }
        } else if (textLength == 6) {
            setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
            setSelection(length());
        } else if (textLength == 10) {
            if (!text.contains("-")) {
                setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                setSelection(length());
            }
        } else if (textLength == 15) {
            if (text.contains("-")) {
                setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                setSelection(length());
            }
        } else if (textLength == 18) {
            if (text.contains("-")) {
                setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                setSelection(length());
            }
        }
    }
}
