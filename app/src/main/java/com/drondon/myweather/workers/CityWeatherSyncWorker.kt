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
 * Last modified 10/29/18 11:46 PM
 */

package com.drondon.myweather.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.drondon.myweather.api.ApiService
import com.drondon.myweather.data.CityWeatherDataSource
import com.drondon.myweather.data.toCityWeatherList
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber


/**
 * Simple [Worker] for sync weather from some hardcoded cities and preload image resources
 * */
class CityWeatherSyncWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    companion object {
        const val INPUT_DATA = "cities"
    }

    private val api: ApiService by inject()
    private val syncManager: SyncManager by inject()
    private val dataSource: CityWeatherDataSource by inject()

    override fun doWork(): Result {
        try {
            val cities = inputData.getIntArray(INPUT_DATA)?.toList() ?: emptyList()
            Timber.d("CityWeatherSyncWorker: load weather for cities: ${cities.joinToString()}")
            val weatherResponse = api.getCitiesWeather(cities)
            val weatherList = weatherResponse.toCityWeatherList()
            return if (weatherList.isNullOrEmpty()) {
                Timber.d("CityWeatherSyncWorker: response RETRY")
                Result.RETRY
            } else {
                dataSource.update(weatherList)
                //TODO: Need to find another way to create periodically chains
                syncManager.preloadWeatherIcons(weatherList.map { it.icon }.toTypedArray())
                Timber.d("CityWeatherSyncWorker: response SUCCESS")
                Result.SUCCESS
            }
        } catch (e: Exception) {
            Timber.e(e, "CityWeatherSyncWorker: response FAILURE")
            return Result.FAILURE
        }
    }
}