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
 * Created by  - Andrew Miroshnychenko on 10/31/18 10:25 PM
 *
 * Last modified 10/31/18 10:25 PM
 */

package com.drondon.myweather.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.drondon.myweather.common.ImageLoader
import com.drondon.myweather.common.WeatherIconSource
import com.drondon.myweather.di.DI
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

/**
 * Image preload worker we use for load images when application in background
 * */
class ImagePreloadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams),
    KoinComponent {

    companion object {
        const val INPUT_DATA = "icons"
    }

    private val imageLoader: ImageLoader by inject(DI.IMAGE_LOADER_CONTEXT)

    override fun doWork(): Result {
        val icons = inputData.getStringArray(INPUT_DATA)
        icons?.forEach { icon ->
            val iconSource = WeatherIconSource(icon)
            imageLoader.load(iconSource, null)
            Timber.d("CityWeatherSyncWorker: preload icon - ${iconSource.getSource()}")
        }
        return Result.SUCCESS
    }
}