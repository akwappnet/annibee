package com.devstree.annibee.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.devstree.annibee.utility.TextUtil.parseDouble
import java.util.*

class PriceEditText : EditText, TextWatcher {
    constructor(context: Context?) : super(context) {
        addTextChangedListener(this)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        addTextChangedListener(this)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun setText(text: CharSequence, type: BufferType) {
        removeTextChangedListener(this)
        super.setText(text, type)
        addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable) {
        val value = parseDouble(s.toString())
        val newValue = String.format(Locale.ENGLISH, "%.3f", value)
        setText(newValue)
    }
}