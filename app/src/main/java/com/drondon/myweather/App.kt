/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:47 AM
 *
 * Last modified 10/30/18 12:08 AM
 */

package com.drondon.myweather

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.drondon.myweather.di.androidModule
import com.drondon.myweather.di.appModule
import com.drondon.myweather.workers.SyncManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : Application() {

    val TAG = "App_"

    private val syncManager: SyncManager by inject()

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    TODO("not implemented") // Add Crashlytics logger
                }
            })
        }

        initDI()
        initSync()
    }

    /*
    * Setup dependencies
    * */
    private fun initDI() {
        startKoin(
            this, listOf(
                appModule,
                androidModule
            )
        )
    }

    /*
     * Setup sync
     * */
    private fun initSync() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun appOnForeground() {
                Timber.tag(TAG).d("appOnForeground")
                syncManager.stopSync()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun appOnBackground() {
                Timber.tag(TAG).d("appOnBackground")
                syncManager.startSync()
            }
        })
    }
}