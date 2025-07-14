package com.example.fitstream.utils.resource

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {
    override fun getResourceString(resId: Int, vararg arg: Any): String {
        return context.resources.getString(resId, *arg)
    }
}