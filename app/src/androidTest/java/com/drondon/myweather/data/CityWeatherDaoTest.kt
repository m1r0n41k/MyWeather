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
 * Created by  - Andrew Miroshnychenko on 11/1/18 3:31 AM
 *
 * Last modified 11/1/18 3:31 AM
 */

package com.drondon.myweather.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.drondon.myweather.comon.TestUtils
import com.drondon.myweather.comon.lambdaMock
import com.drondon.myweather.comon.lifecycle
import com.drondon.myweather.comon.owner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mockito.verify
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class CityWeatherDaoTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var appDataBase: AppDataBase? = null
    private var dao: CityWeatherDao? = null

    @Before
    fun setUp() {
        appDataBase = AppDataBase.buildDatabase(ApplicationProvider.getApplicationContext(), true)
        dao = appDataBase!!.cityWeatherDao()
    }

    @After
    fun tearDown() {
        appDataBase?.close()
    }

    @Test
    fun insertAll() {
        assertNotNull(appDataBase)
        val dao = this.dao
        assertNotNull(dao)

        val list = listOf(TestUtils.createCityWeather(1))
        dao.insertAll(list)


        val allLiveData = dao.getAll()

        val observer = lambdaMock<(List<CityWeather>) -> Unit>()
        allLiveData.observe(lifecycle().owner(), Observer { observer(it) })

        //Check invocation
        verify(observer).invoke(list)

        val value = allLiveData.value
        assertNotNull(value)
        assertEquals(1, value.size)
    }

    @Test
    fun delete() {
        assertNotNull(appDataBase)
        val dao = this.dao
        assertNotNull(dao)

        val list = listOf(TestUtils.createCityWeather(1))
        dao.insertAll(list)

        val allLiveData = dao.getAll()
        val observer = lambdaMock<(List<CityWeather>) -> Unit>()
        allLiveData.observe(lifecycle().owner(), Observer { observer(it) })

        //Check invocation
        verify(observer).invoke(list)

        val value = allLiveData.value

        assertNotNull(value)
        assertEquals(1, value.size)

        //Delete all from table
        dao.delete(list)

        val valueAfterDelete = allLiveData.value
        assertNotNull(valueAfterDelete)
        assertEquals(0, valueAfterDelete.size)
    }
}