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
 * Created by  - Andrew Miroshnychenko on 11/1/18 1:36 AM
 *
 * Last modified 11/1/18 1:33 AM
 */

package com.drondon.myweather.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.work.impl.constraints.NetworkState
import androidx.work.impl.constraints.trackers.NetworkStateTracker
import com.drondon.myweather.comon.lambdaMock
import com.drondon.myweather.comon.lifecycle
import com.drondon.myweather.comon.owner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class NetworkLiveDataTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    @UiThreadTest
    fun onActive() {
        val networkTracker = NetworkStateTracker(ApplicationProvider.getApplicationContext())

        val liveData = NetworkLiveData(networkTracker)


        // Mock function for assert invocation
        val observer = lambdaMock<(NetworkState?) -> Unit>()

        //Observe live data with mocked lifecycle
        liveData.observe(lifecycle().owner(), Observer { observer(it) })

        val state = mock(NetworkState::class.java)
        //Change state
        networkTracker.setState(state)

        //Validate state changed
        verify(observer).invoke(state)
    }

    @Test
    @UiThreadTest
    fun onInactive() {
        val networkTracker = NetworkStateTracker(ApplicationProvider.getApplicationContext())

        val liveData = NetworkLiveData(networkTracker)

        // Mock function for assert invocation
        val observer = lambdaMock<(NetworkState?) -> Unit>()

        //Observe live data with mocked lifecycle
        liveData.observe(lifecycle(Lifecycle.Event.ON_DESTROY).owner(), Observer { observer(it) })

        val state = mock(NetworkState::class.java)
        //Change state
        networkTracker.setState(state)

        //Validate state  not changed
        verify(observer, never()).invoke(state)
    }

}