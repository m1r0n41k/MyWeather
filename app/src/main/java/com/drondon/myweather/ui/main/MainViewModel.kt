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
 * Last modified 10/29/18 11:20 PM
 */

package com.drondon.myweather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import androidx.work.impl.constraints.trackers.Trackers
import com.drondon.myweather.data.CityWeather
import com.drondon.myweather.data.CityWeatherDataSource
import com.drondon.myweather.workers.CityWeatherSyncWorker
import timber.log.Timber

class MainViewModel(dataSource: CityWeatherDataSource, trackers: Trackers) : ViewModel() {

    private val _networkStateLiveData = NetworkLiveData(trackers)

    val networkState = Transformations.map(_networkStateLiveData) {
        Pair(it, cityWeather.value?.minBy { city -> city.modifiedTime }?.modifiedTime ?: 0)
    }

    val cityWeather: LiveData<List<CityWeather>> = dataSource.loadAll()

    private val _progressLiveData = MediatorLiveData<Boolean>().apply {
        /*
        * Hide progress when cityWeather changed.
        * */
        addSource(Transformations.map(cityWeather) { false }) {
            this.value = it
        }
    }

    val progress: LiveData<Boolean>
        get() = _progressLiveData

    private var statusProgressLiveData: LiveData<Boolean>? = null

    fun refresh() {
        forceRefresh().apply {
            /*Convert work status to progress state*/
            registerProgresSource()
        }
    }

    private fun LiveData<WorkStatus>.registerProgresSource() {
        statusProgressLiveData?.let {
            _progressLiveData.removeSource(it)
        }

        statusProgressLiveData = Transformations.map(this) {
            !it.state.isFinished
        }

        statusProgressLiveData?.apply {
            _progressLiveData.addSource(this) {
                _progressLiveData.value = it
            }
        }
    }

    /*
    * Create worker request and return status LiveData
    * */
    private fun forceRefresh(): LiveData<WorkStatus> {
        val request = OneTimeWorkRequestBuilder<CityWeatherSyncWorker>()
            .addTag(TAG_REFRESH)
            .build()

        val workManager = WorkManager.getInstance()
        workManager.cancelAllWorkByTag(TAG_REFRESH)
        workManager.enqueue(request)
        Timber.d("Sync: Work force refresh, %s", request.toString())
        return workManager.getStatusByIdLiveData(request.id)
    }

    /**
     * Change
     * */
    fun hideProgress() {
        _progressLiveData.value = false
    }

}
