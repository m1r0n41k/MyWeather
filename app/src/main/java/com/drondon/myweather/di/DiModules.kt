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

package com.drondon.myweather.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.work.WorkManager
import androidx.work.impl.constraints.trackers.Trackers
import com.drondon.myweather.api.ApiData
import com.drondon.myweather.api.ApiService
import com.drondon.myweather.api.ApiServiceImpl
import com.drondon.myweather.common.*
import com.drondon.myweather.data.AppDataBase
import com.drondon.myweather.data.CityWeatherDataSource
import com.drondon.myweather.data.CityWeatherRepository
import com.drondon.myweather.di.DI.DIR_CACHE
import com.drondon.myweather.di.DI.INTERCEPTOR_HTTP_LOGGER
import com.drondon.myweather.extensions.MB
import com.drondon.myweather.ui.main.MainViewModel
import com.drondon.myweather.workers.SyncManager
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import timber.log.Timber
import java.util.concurrent.Executor

/**
 * Specify constants for similar types injections
 * */
object DI {

    /*Executors*/
    /**
     * IO executor for long tasks (write to data base etc.)
     * */
    const val EXECUTOR_IO = "executor-io"

    /**
     * Provide access to main [Thread] executor over [android.os.Handler]
     * */
    const val EXECUTOR_UI = "executor-ui"

    /*Image loaders*/

    /**
     * Image loader implementation depends on fragment lifecycle
     * */
    const val IMAGE_LOADER_FRAGMENT = "image-loader-fragment"

    /**
     * Image loader implementation depends on context
     * */
    const val IMAGE_LOADER_CONTEXT = "image-loader-context"

    /*Cache*/
    /**
     * Provide dependency to cache directory [java.io.File] instance
     * */
    const val DIR_CACHE = "dir-cache"

    /*Network*/

    const val INTERCEPTOR_HTTP_LOGGER = "interceptor-http-logger"
}

/**
 * Provide basic android components
 * */
@SuppressLint("RestrictedApi")
val androidModule = module {
    single(DIR_CACHE) { get<Context>().cacheDir }

    // DB
    single { AppDataBase.buildDatabase(get()) }

    // Executors
    single(DI.EXECUTOR_IO) { IO_EXECUTOR as Executor }
    single(DI.EXECUTOR_UI) { UI_EXECUTOR }

    viewModel { MainViewModel(get(), get(), get()) }

    factory(DI.IMAGE_LOADER_FRAGMENT) { parameterList -> FragmentImageLoader(parameterList.get<Fragment>() as Fragment) as ImageLoader }
    single(DI.IMAGE_LOADER_CONTEXT) { ContextImageLoader(get()) as ImageLoader }

    single { Trackers.getInstance(get()) }

    single { ApiData("https://api.openweathermap.org/data/2.5", "a1d1dc41d71e2b1c1d329e64770bf088") }

    single { WorkManager.getInstance() }
}


/**
 * Provide basic non android components
 * */
val appModule = module {
    single { Cache(get(DIR_CACHE), 20L.MB) }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get(INTERCEPTOR_HTTP_LOGGER))
            .cache(get())
            .build()
    }
    single(INTERCEPTOR_HTTP_LOGGER) {
        HttpLoggingInterceptor(Logger { message ->
            Timber.tag("OkHttp: ").d(message)
        }) as Interceptor
    }
    single { Moshi.Builder().build() }
    single { AppExecutorsImpl(get(DI.EXECUTOR_IO), get(DI.EXECUTOR_UI)) as AppExecutors }

    // Data sources
    single { get<AppDataBase>().cityWeatherDao() }
    single { CityWeatherRepository(get(), get()) as CityWeatherDataSource }

    single { ApiServiceImpl(get(), get(), get()) as ApiService }

    single { SyncManager(get()) }
}