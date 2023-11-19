package com.example.calculator

import android.app.Application
import android.content.Context

class BaseApplication:Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var appVarHolder: ApplicationDataHolder
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        appVarHolder = ApplicationDataHolder
    }
}