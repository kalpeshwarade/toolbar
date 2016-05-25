package com.hska.ebusiness.toolbar.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class TextValidator implements TextWatcher {
    private final EditText editText;

    public TextValidator(final EditText editText) {
        this.editText = editText;
    }

    public abstract void validate(final EditText editText, final String text);

    @Override
    final public void afterTextChanged(final Editable s) {
        final String text = editText.getText().toString();
        validate(editText, text);
    }

    @Override
    final public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        // Do nothing
    }

    @Override
    final public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        // Do nothing
    }
}
