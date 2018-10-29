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
 * Last modified 10/30/18 12:33 AM
 */

package com.drondon.myweather.data

import androidx.lifecycle.LiveData

/**
 * City screen Model
 *
 * Provide access to [CityWeather]s data source. Local copy of most resent data stored in local data base
 * */
interface CityWeatherDataSource {

    /**
     * Return [LiveData] to [CityWeather] data source
     * */
    fun loadAll(): LiveData<List<CityWeather>>

    /**
     * Update [CityWeather]s in data source
     * */
    fun update(data: List<CityWeather>)

}