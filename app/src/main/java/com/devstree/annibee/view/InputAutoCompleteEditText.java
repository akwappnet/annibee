package com.devstree.annibee.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class InputAutoCompleteEditText extends MaterialAutoCompleteTextView {

    private int threshold = 0;

    public InputAutoCompleteEditText(@NonNull Context context) {
        super(context);
    }

    public InputAutoCompleteEditText(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public InputAutoCompleteEditText(@NonNull Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    public int getThreshold() {
        return threshold;
    }


    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (getFilter() == null) return;
        if (focused) {
            performFiltering("", 0);
            showDropDown();
        }
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
    }
}
