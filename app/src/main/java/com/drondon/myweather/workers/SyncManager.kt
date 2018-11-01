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
 * Created by  - Andrew Miroshnychenko on 10/30/18 11:05 PM
 *
 * Last modified 10/30/18 10:57 PM
 */

package com.drondon.myweather.workers

import androidx.work.*
import com.drondon.myweather.Constants
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class SyncManager(private val workManager: WorkManager) {

    companion object {
        internal const val TAG_SYNC = "Sync"
        internal const val TAG_PRELOAD = "Preload"
        // Create a Constraints object that defines when the task should run
        private val appInBackgroundConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .build()
    }

    /**
     *  Setup and enqueue
     * */
    fun startSync(cities: IntArray = Constants.DEFAULT_CITIES): UUID {
        val inputData = Data.Builder().putIntArray(CityWeatherSyncWorker.INPUT_DATA, cities).build()
        val weatherRequest = PeriodicWorkRequestBuilder<CityWeatherSyncWorker>(1, TimeUnit.HOURS, 50, TimeUnit.MINUTES)
            .setConstraints(appInBackgroundConstraints)
            .addTag(TAG_SYNC)
            .setInputData(inputData)
            .build()
        workManager.enqueueUniquePeriodicWork(TAG_SYNC, ExistingPeriodicWorkPolicy.REPLACE, weatherRequest)
        Timber.d("SyncManager: start with: %s", weatherRequest.toString())
        return weatherRequest.id
    }

    /**
     *  Preload weather icons
     * */
    fun preloadWeatherIcons(icons: Array<String>): UUID {
        val inputData = Data.Builder().putStringArray(ImagePreloadWorker.INPUT_DATA, icons).build()
        val preloadRequest = OneTimeWorkRequestBuilder<ImagePreloadWorker>()
            .setConstraints(appInBackgroundConstraints)
            .addTag(TAG_PRELOAD)
            .setInputData(inputData)
            .build()
        workManager.enqueue(preloadRequest)
        Timber.d("SyncManager: start preload weather icons: %s", preloadRequest.toString())
        return preloadRequest.id
    }

    fun stopSync() {
        Timber.d("SyncManager: stop")
        workManager.cancelUniqueWork(TAG_SYNC)
        workManager.cancelAllWorkByTag(TAG_SYNC)
        workManager.cancelAllWorkByTag(TAG_PRELOAD)
    }
}