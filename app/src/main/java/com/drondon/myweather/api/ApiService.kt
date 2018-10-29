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
 * Last modified 10/30/18 12:01 AM
 */

package com.drondon.myweather.api

import com.drondon.myweather.data.WeatherResponse
import com.drondon.myweather.data.WeatherResponseJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ApiServiceImpl(
    private val apiData: ApiData,
    private val client: OkHttpClient,
    private val moshi: Moshi
) :
    ApiService {

    private fun get(url: String, withAppId: Boolean = true) =
        Request.Builder().url(url + if (withAppId) "&appid=${apiData.appId}" else "").get().build()

    @Throws(IllegalStateException::class, IOException::class, KotlinNullPointerException::class)
    override fun getCitiesWeather(cities: List<Int>): WeatherResponse {
        val url = "${apiData.baseUrl}/group?" + "id=${cities.joinToString(separator = ",")}&units=metric"
        val responseBody = client.newCall(get(url)).execute().body()
        return WeatherResponseJsonAdapter(moshi).fromJson(responseBody!!.string())!!
    }
}


interface ApiService {
    fun getCitiesWeather(cities: List<Int>): WeatherResponse
}

data class ApiData(val baseUrl: String, val appId: String)