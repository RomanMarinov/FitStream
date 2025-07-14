package com.example.fitstream.utils.resource

import android.content.Context
import com.example.fitstream.R
import jakarta.inject.Inject

class AndroidResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {
    override fun getResourceString(resId: Int, vararg arg: Any): String {
        return context.resources.getString(R.string.error_download_video)
    }
}
