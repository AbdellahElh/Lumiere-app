package com.example.riseandroid

import android.app.Application
import com.example.riseandroid.data.AppContainer
import com.example.riseandroid.data.DefaultAppContainer


class LumiereApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}






