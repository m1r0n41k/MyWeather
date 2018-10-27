package com.drondon.myweather

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate()
    }
}