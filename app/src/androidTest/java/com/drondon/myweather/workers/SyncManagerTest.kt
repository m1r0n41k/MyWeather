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
 * Created by  - Andrew Miroshnychenko on 10/31/18 11:41 PM
 *
 * Last modified 10/31/18 11:41 PM
 */

package com.drondon.myweather.workers

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.work.State
import androidx.work.WorkManager
import androidx.work.test.WorkManagerTestInitHelper
import com.drondon.myweather.api.ApiService
import com.drondon.myweather.common.TestImageLoader
import com.drondon.myweather.comon.TestUtils
import com.drondon.myweather.comon.empty
import com.drondon.myweather.comon.lifecycle
import com.drondon.myweather.comon.owner
import com.drondon.myweather.data.AppDataBase
import com.drondon.myweather.data.WeatherResponse
import com.drondon.myweather.di.DI
import com.drondon.myweather.di.appModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@MediumTest
class SyncManagerTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val syncManager: SyncManager by inject()
    private val db: AppDataBase by inject()
    private val workManager: WorkManager by inject()

    @Before
    fun setUp() {

        stopKoin()
        val context = ApplicationProvider.getApplicationContext<Context>()
        startKoin(listOf(
            appModule,
            module {
                single {
                    WorkManagerTestInitHelper.initializeTestWorkManager(context)
                    WorkManager.getInstance()
                }
                single { AppDataBase.buildDatabase(context, true) }

                //Mock server response
                single {
                    object : ApiService {
                        override fun getCitiesWeather(cities: List<Int>): WeatherResponse {
                            return TestUtils.createWeatherResponse()
                        }

                    }
                }
                single(DI.IMAGE_LOADER_CONTEXT) { TestImageLoader() }
            }

        ))
    }

    @After
    fun tearDown() {
        db.close()
        stopKoin()
    }

    @Test
    @UiThreadTest
    fun startSync() {
        val uuid = syncManager.startSync(intArrayOf(1))
        WorkManagerTestInitHelper.getTestDriver().apply {
            setAllConstraintsMet(uuid)
            setPeriodDelayMet(uuid)
        }
        assertNotNull(db.cityWeatherDao().getAll())
    }

    @Test
    fun preloadWeatherIcons() {
    }

    @Test
    @UiThreadTest
    fun stopSync() {
        val uuid = syncManager.startSync(intArrayOf(1))

        WorkManagerTestInitHelper.getTestDriver().apply {
            setAllConstraintsMet(uuid)
            setPeriodDelayMet(uuid)
        }


        val liveData = workManager.getStatusesByTagLiveData(SyncManager.TAG_SYNC)

        liveData.observe(lifecycle().owner(), empty())

        assertEquals(1, liveData.value?.size)

        syncManager.stopSync()

        val afterStopLiveData = workManager.getStatusesByTagLiveData(SyncManager.TAG_SYNC)

        afterStopLiveData.observe(lifecycle().owner(), empty())

        assertEquals(1, afterStopLiveData.value?.size)
        
        assertEquals(State.CANCELLED, afterStopLiveData.value?.first()?.state)
    }
}