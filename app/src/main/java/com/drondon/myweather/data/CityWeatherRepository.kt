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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:45 AM
 *
 * Last modified 10/30/18 12:37 AM
 */

package com.drondon.myweather.data

import androidx.lifecycle.LiveData
import com.drondon.myweather.common.AppExecutors

/**
 * [CityWeatherDataSource] implementation witch provide access to Disc cache (SQLite database)
 *
 * @constructor create [CityWeatherRepository] with current implementation of [CityWeatherDao] table accessor and [AppExecutors]
 * */
class CityWeatherRepository(
    private val cityWeatherDao: CityWeatherDao,
    private val appExecutors: AppExecutors
) : CityWeatherDataSource {

    /**
     * Update all [CityWeather]s in data base. Work asynchronous on [AppExecutors]
     *
     * @param data list or empty list of [CityWeather]s
     * @see AppExecutors
     * */
    override fun update(data: List<CityWeather>) {
        appExecutors.runOnIoThread {
            cityWeatherDao.insertAll(data)
        }
    }

    /**
     * Load [List] of [CityWeather]s from data base.
     *
     * @return [LiveData] accessor to all items from [CityWeatherDao] table
     * */
    override fun loadAll(): LiveData<List<CityWeather>> {
        return cityWeatherDao.getAll()
    }
}