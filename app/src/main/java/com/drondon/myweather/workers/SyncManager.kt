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

import androidx.lifecycle.LiveData
import androidx.work.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SyncManager(private val workManager: WorkManager) {

    companion object {
        private const val TAG_SYNC = "Sync"
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
    fun startSync(): LiveData<WorkStatus> {
        val request = PeriodicWorkRequestBuilder<CityWeatherSyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(appInBackgroundConstraints)
            .addTag(TAG_SYNC)
            .build()
        workManager.enqueueUniquePeriodicWork(TAG_SYNC, ExistingPeriodicWorkPolicy.REPLACE, request)
        Timber.d("SyncManager: start with: %s", request.toString())
        return workManager.getStatusByIdLiveData(request.id)

    }

    fun stopSync() {
        Timber.d("SyncManager: stop")
        workManager.cancelAllWorkByTag(TAG_SYNC)
    }
}