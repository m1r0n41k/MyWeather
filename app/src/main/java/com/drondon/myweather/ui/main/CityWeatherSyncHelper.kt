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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:46 AM
 *
 * Last modified 10/30/18 12:08 AM
 */

package com.drondon.myweather.ui.main

import androidx.lifecycle.LiveData
import androidx.work.*
import com.drondon.myweather.workers.CityWeatherSyncWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val TAG_REFRESH = "Refresh"
const val TAG_SYNC = "Sync"

// Create a Constraints object that defines when the task should run
val appInBackgroundConstraints = Constraints.Builder()
    .setRequiresBatteryNotLow(true)
    .setRequiresStorageNotLow(true)
    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
    .build()


fun refresh() {
    val launchedWorkers = WorkManager.getInstance().getStatusesByTag(TAG_REFRESH).get()
    if (launchedWorkers?.any { it.state == State.ENQUEUED } == true) {
        launchedWorkers.forEach {
            Timber.d("Refresh: Work status: %s", it.toString())
        }
    } else {
        val request = OneTimeWorkRequestBuilder<CityWeatherSyncWorker>()
            .setConstraints(appInBackgroundConstraints)
            .addTag(TAG_REFRESH)
            .build()
        WorkManager.getInstance().enqueue(request)
    }
}

fun setupSync(): LiveData<WorkStatus> {
    val workManager = WorkManager.getInstance()
    val request = PeriodicWorkRequestBuilder<CityWeatherSyncWorker>(1, TimeUnit.HOURS)
        .setConstraints(appInBackgroundConstraints)
        .addTag(TAG_SYNC)
        .build()
    workManager.enqueueUniquePeriodicWork(TAG_SYNC, ExistingPeriodicWorkPolicy.REPLACE, request)
    Timber.d("Sync: Work setup, %s", request.toString())
    return workManager.getStatusByIdLiveData(request.id)

}

fun cancelSync() {
    WorkManager.getInstance().cancelAllWorkByTag(TAG_SYNC)
}