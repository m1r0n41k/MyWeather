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
 * Last modified 10/28/18 11:45 PM
 */

package com.drondon.myweather

import com.drondon.myweather.data.WeatherResponseJsonAdapter
import com.drondon.myweather.di.appModule
import com.squareup.moshi.Moshi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


val TEST_JSON = """
    {
    "cnt": 3,
    "list": [
        {
            "coord": {
                "lon": 30.52,
                "lat": 50.43
            },
            "sys": {
                "type": 1,
                "id": 7348,
                "message": 0.1497,
                "country": "UA",
                "sunrise": 1540701768,
                "sunset": 1540737582
            },
            "weather": [
                {
                    "id": 802,
                    "main": "Clouds",
                    "description": "scattered clouds",
                    "icon": "03n"
                }
            ],
            "main": {
                "temp": 10.33,
                "pressure": 1016,
                "humidity": 93,
                "temp_min": 10,
                "temp_max": 11
            },
            "visibility": 8000,
            "wind": {
                "speed": 3,
                "deg": 60
            },
            "clouds": {
                "all": 44
            },
            "dt": 1540752598,
            "id": 703448,
            "name": "Kiev"
        },
        {
            "coord": {
                "lon": -79.42,
                "lat": 43.7
            },
            "sys": {
                "type": 1,
                "id": 3722,
                "message": 0.0048,
                "country": "CA",
                "sunrise": 1540727361,
                "sunset": 1540764766
            },
            "weather": [
                {
                    "id": 500,
                    "main": "Rain",
                    "description": "light rain",
                    "icon": "10d"
                }
            ],
            "main": {
                "temp": 4.59,
                "pressure": 1004,
                "humidity": 81,
                "temp_min": 4,
                "temp_max": 6
            },
            "visibility": 24140,
            "wind": {
                "speed": 4.1,
                "deg": 240
            },
            "clouds": {
                "all": 90
            },
            "dt": 1540752598,
            "id": 6167865,
            "name": "Toronto"
        },
        {
            "coord": {
                "lon": -0.13,
                "lat": 51.51
            },
            "sys": {
                "type": 1,
                "id": 5091,
                "message": 0.0026,
                "country": "GB",
                "sunrise": 1540709286,
                "sunset": 1540744774
            },
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01n"
                }
            ],
            "main": {
                "temp": 5.18,
                "pressure": 1018,
                "humidity": 75,
                "temp_min": 3,
                "temp_max": 7
            },
            "visibility": 10000,
            "wind": {
                "speed": 1.5
            },
            "clouds": {
                "all": 0
            },
            "dt": 1540752598,
            "id": 2643743,
            "name": "London"
        }
    ]
}
""".trimIndent()


class JsonModelsTest : KoinTest {

    private val moshi: Moshi by inject()

    @Before
    fun setUp() {
        startKoin(listOf(appModule))
    }

    @Test
    fun validJsonResponse() {
        val adapter = WeatherResponseJsonAdapter(moshi)
        val weather = adapter.fromJson(TEST_JSON)
        assertNotNull(weather)
        assertEquals(3, weather.cnt, "Invalid count")
        assertEquals(weather.cnt.toInt(), weather.list.size, "Invalid size")
        val city = weather.list.firstOrNull()
        assertNotNull(city, "Cities can't be empty")

        city.apply {
            assertEquals("Kiev", name)
            assertEquals(50.43, coord.lat)
            assertEquals(30.52, coord.lon)
            assertEquals(1, this.weather.size)
            // etc.
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}