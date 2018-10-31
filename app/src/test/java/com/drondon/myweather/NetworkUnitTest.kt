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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:48 AM
 *
 * Last modified 10/30/18 12:01 AM
 */

package com.drondon.myweather

import com.drondon.myweather.api.ApiData
import com.drondon.myweather.api.ApiService
import com.drondon.myweather.di.DI
import com.drondon.myweather.di.appModule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.get
import org.koin.standalone.inject
import org.koin.test.KoinTest
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NetworkUnitTest : KoinTest {
    private val cities = listOf(703448, 6167865, 2643743)
    private val apiService: ApiService by inject()

    @Before
    fun setUp() {
        startKoin(listOf(
            appModule,
            module {
                single(DI.DIR_CACHE) { File("./build/tmp") }
                single { ApiData("http://api.openweathermap.org/data/2.5", "a1d1dc41d71e2b1c1d329e64770bf088") }

            }
        ))
    }

    @Test
    fun testWeatherServer() {
        val weatherResponse = apiService.getCitiesWeather(cities)
        assertNotNull(weatherResponse)
        assertEquals(3, weatherResponse.cnt)
        val cities = weatherResponse.list
        assertEquals(weatherResponse.cnt.toInt(), cities.size)

        assertEquals(703448, cities.first().id, "First city must be Kiev with id:")
        assertEquals(6167865, cities[1].id, "First city must be Toronto with id:")
        assertEquals(2643743, cities[2].id, "First city must be London with id:")
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}