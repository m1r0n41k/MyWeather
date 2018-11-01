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
 * Created by  - Andrew Miroshnychenko on 11/1/18 4:25 AM
 *
 * Last modified 11/1/18 4:25 AM
 */

package com.drondon.myweather.comon

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.drondon.myweather.data.*
import org.mockito.Mockito.mock

inline fun <reified T> lambdaMock(): T = mock(T::class.java)

fun Lifecycle.owner() = LifecycleOwner { this }

fun <T> empty(): Observer<T> {
    return Observer { }
}

fun lifecycle(event: Lifecycle.Event = Lifecycle.Event.ON_RESUME): Lifecycle {
    val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
    lifecycle.handleLifecycleEvent(event)
    return lifecycle
}

object TestUtils {
    fun createCityWeather(id: Long, time: Long = 123456789) = CityWeather(id, "001", "Test", "UA", "Test", -10.0, time)

    fun createWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            1,
            listOf(
                City(
                    AdditionalInfo("UA"),
                    listOf(WeatherInfo("Test", "001")),
                    MainInfo(-10.0),
                    System.currentTimeMillis() / 1000,
                    1L,
                    "Test"
                )
            )
        )
    }
}