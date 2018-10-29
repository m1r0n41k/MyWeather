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
 * Last modified 10/29/18 7:27 PM
 */

package com.drondon.myweather.utils

import android.os.SystemClock


/**
 * A Debounced [DebounceEvent]
 * Rejects events that are too close together in time.
 */
abstract class DebounceEvent(private val minimumInterval: Long = 0) {
    private var lastEventTime: Long = Long.MIN_VALUE

    abstract fun onDebouncedEvent()

    protected open fun onSkippedEvent() {}

    fun performEvent() {
        val previousClickTimestamp = lastEventTime
        val currentTimestamp = SystemClock.uptimeMillis()
        if (Math.abs(currentTimestamp - previousClickTimestamp) > minimumInterval) {
            lastEventTime = currentTimestamp
            onDebouncedEvent()
        } else {
            onSkippedEvent()
        }
    }

}