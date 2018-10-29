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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:44 AM
 *
 * Last modified 10/30/18 12:37 AM
 */

package com.drondon.myweather.data

/**
 * Utility converter for convert [WeatherResponse] to [List] of [CityWeather]s
 *
 * @receiver non null instance of [WeatherResponse]
 * @return [List] or empty [List] of [CityWeather]
 * */
fun WeatherResponse.toCityWeatherList(): List<CityWeather> = list.map { city ->
    val weatherInfo = city.weather.first()
    val mainInfo = city.main
    CityWeather(
        id = city.id,
        icon = weatherInfo.icon,
        name = city.name,
        country = city.sys.country,
        description = weatherInfo.main,
        temperature = mainInfo.temp,
        modifiedTime = city.dt
    )
}