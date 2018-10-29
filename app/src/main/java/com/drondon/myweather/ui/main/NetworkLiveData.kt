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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:46 AM
 *
 * Last modified 10/29/18 10:26 PM
 */

package com.drondon.myweather.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.work.impl.constraints.ConstraintListener
import androidx.work.impl.constraints.NetworkState
import androidx.work.impl.constraints.trackers.Trackers


class NetworkLiveData(private val trackers: Trackers) : LiveData<NetworkState?>(), ConstraintListener<NetworkState> {
    override fun onConstraintChanged(newValue: NetworkState?) {
        postValue(newValue)
    }

    @SuppressLint("RestrictedApi")
    override fun onActive() {
        super.onActive()
        trackers.networkStateTracker.addListener(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onInactive() {
        super.onInactive()
        trackers.networkStateTracker.removeListener(this)
    }
}