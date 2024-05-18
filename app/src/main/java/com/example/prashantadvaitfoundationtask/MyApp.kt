package com.example.prashantadvaitfoundationtask

import android.app.Application
import com.example.prashantadvaitfoundationtask.utils.DiskCacheUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DiskCacheUtils.init(this)
        Timber.plant(Timber.DebugTree())
    }
}