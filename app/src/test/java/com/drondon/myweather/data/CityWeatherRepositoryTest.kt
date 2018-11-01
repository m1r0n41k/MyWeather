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
 * Created by  - Andrew Miroshnychenko on 11/1/18 2:46 AM
 *
 * Last modified 11/1/18 2:46 AM
 */

package com.drondon.myweather.data

import androidx.lifecycle.LiveData
import com.drondon.myweather.common.AppExecutorsImpl
import com.drondon.myweather.common.TEST_EXECUTOR
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class CityWeatherRepositoryTest {

    private inline fun <reified T> reifiedMock(): T = mock(T::class.java)


    @Test
    fun update() {
        val dao = mock(CityWeatherDao::class.java)

        val repository = CityWeatherRepository(dao, AppExecutorsImpl(TEST_EXECUTOR, TEST_EXECUTOR))

        val list = listOf(mock(CityWeather::class.java))

        repository.update(list)

        verify(dao).insertAll(list)
    }

    @Test
    fun loadAll() {

        val dao = mock(CityWeatherDao::class.java)
        val liveData = reifiedMock<LiveData<List<CityWeather>>>()
        `when`(dao.getAll()).thenReturn(liveData)

        val repository = CityWeatherRepository(dao, AppExecutorsImpl(TEST_EXECUTOR, TEST_EXECUTOR))

        assertEquals(liveData, repository.loadAll())

    }
}