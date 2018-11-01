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
 * Created by  - Andrew Miroshnychenko on 11/1/18 2:40 AM
 *
 * Last modified 11/1/18 2:40 AM
 */

package com.drondon.myweather.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeatherConverterKtTest {

    @Test
    fun toCityWeatherList() {
        val response = createWeatherResponse()

        val list = response.toCityWeatherList()
        assertEquals(1, list.size)
        val city = list.firstOrNull()
        assertNotNull(city)
        assertEquals(1L, city.id)
        assertEquals("Test", city.name)
        assertEquals("UA", city.country)
        assertEquals(-10.0, city.temperature)
        assertEquals(123456789, city.modifiedTime)
        assertEquals("Test", city.description)
        assertEquals("001", city.icon)
    }

    private fun createWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            1,
            listOf(
                City(
                    AdditionalInfo("UA"),
                    listOf(WeatherInfo("Test", "001")),
                    MainInfo(-10.0),
                    123456789,
                    1L,
                    "Test"
                )
            )
        )
    }
}