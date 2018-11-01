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
 * Last modified 10/30/18 12:09 AM
 */

package com.drondon.myweather

import androidx.work.WorkManager
import com.drondon.myweather.api.ApiConfiguration
import com.drondon.myweather.common.TEST_EXECUTOR
import com.drondon.myweather.data.AppDataBase
import com.drondon.myweather.di.DI
import com.drondon.myweather.di.apiModule
import com.drondon.myweather.di.appModule
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.test.KoinTest
import org.koin.test.checkModules
import org.mockito.Mockito.mock
import java.io.File

class DITest : KoinTest {

    //Only for validate DI graph
    private val appDataBase = mock(AppDataBase::class.java)
    private val workManager = mock(WorkManager::class.java)

    @Test
    fun checkModulesTest() {
        //Validate all non-Android modules
        checkModules(
            listOf(
                appModule,
                apiModule,
                module {
                    single(DI.DIR_CACHE) { File("/tmp") }
                    single { appDataBase }
                    single(DI.EXECUTOR_IO) { TEST_EXECUTOR }
                    single(DI.EXECUTOR_UI) { TEST_EXECUTOR }
                    single { ApiConfiguration("", "") }
                    single { workManager } //Use WorkManagerTestInitHelper for Android tests
                }
            )
        )
    }
}