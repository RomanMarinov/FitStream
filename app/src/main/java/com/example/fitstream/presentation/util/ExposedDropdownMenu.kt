package com.example.fitstream.presentation.util

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ExposedDropdownMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialAutoCompleteTextView(context, attrs) {

    override fun getFreezesText(): Boolean {
        return false
    }
}