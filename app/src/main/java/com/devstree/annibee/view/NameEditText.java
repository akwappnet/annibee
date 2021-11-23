package com.devstree.annibee.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class NameEditText extends EditText implements TextWatcher {


    public NameEditText(Context context) {
        super(context);
        addTextChangedListener(this);
    }

    public NameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    public NameEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    int mStart = 0;
    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        String capitalizedText;
        if (input.length() < 1)
            capitalizedText = input;
        else if (input.length() > 1 && input.contains(" ")) {
            String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
            if (fstr.length() == input.length()) {
                capitalizedText = fstr;
            } else {
                String sstr = input.substring(input.lastIndexOf(" ") + 1);
                sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                capitalizedText = fstr + sstr;
            }
        } else
            capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);
        if (!capitalizedText.equals(trim())) {
           addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mStart = start + count;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setSelection(mStart);
                    removeTextChangedListener(this);
                }
            });
            setText(capitalizedText);
        }
    }
}
