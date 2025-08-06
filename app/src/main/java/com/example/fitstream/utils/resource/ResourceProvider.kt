package com.example.fitstream.utils.resource

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getResourceString(@StringRes resId: Int, vararg arg: Any): String
}