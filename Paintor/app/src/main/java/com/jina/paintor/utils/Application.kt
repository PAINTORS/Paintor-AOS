package com.jina.paintor.utils

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val strategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("PAINTOR")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
//            }
        })
    }
}