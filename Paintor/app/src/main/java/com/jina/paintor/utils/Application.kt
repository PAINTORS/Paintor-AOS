package com.jina.paintor.utils

import android.app.Application
import com.jina.paintor.database.AppDatabase
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class Application : Application() {

    init {
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: com.jina.paintor.utils.Application
    }

    val database by lazy { AppDatabase.getInstance(applicationContext) }

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