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
 * Last modified 10/30/18 12:43 AM
 */

package com.drondon.myweather.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(val cnt: Long, val list: List<City>)

@JsonClass(generateAdapter = true)
class WeatherInfo(val id: Long, val main: String, val description: String, val icon: String)

@JsonClass(generateAdapter = true)
class AdditionalInfo(
    val type: Long,
    val id: Long,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

@JsonClass(generateAdapter = true)
class MainInfo(
    val temp: Double,
    val pressure: Long,
    val humidity: Long,
    val temp_min: Long,
    val temp_max: Long
)

@JsonClass(generateAdapter = true)
class City(
    val sys: AdditionalInfo,
    val weather: List<WeatherInfo>,
    val main: MainInfo,
    val visibility: Long,
    val dt: Long,
    val id: Long,
    val name: String
)