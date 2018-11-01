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
 * Created by  - Andrew Miroshnychenko on 11/1/18 1:18 AM
 *
 * Last modified 11/1/18 1:18 AM
 */

package com.drondon.myweather.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DebounceEventTest {

    @Test
    fun onDebouncedEvent() {
        var debounceCounter = 0
        val debounceEvent = object : DebounceEvent(50) {
            override fun onDebouncedEvent() {
                debounceCounter++
            }
        }
        assertEquals(0, debounceCounter)
        debounceEvent.performEvent()
        assertEquals(1, debounceCounter)
        debounceEvent.performEvent()
        debounceEvent.performEvent()
        assertEquals(1, debounceCounter)
        Thread.sleep(50)
        debounceEvent.performEvent()
        debounceEvent.performEvent()
        assertEquals(2, debounceCounter)
    }

    @Test
    fun onSkippedEvent() {

        var skippedCounter = 0
        val debounceEvent = object : DebounceEvent(50) {
            override fun onDebouncedEvent() {
                //ignore
            }

            override fun onSkippedEvent() {
                skippedCounter++
            }
        }
        assertEquals(0, skippedCounter)
        debounceEvent.performEvent()
        assertEquals(0, skippedCounter)
        debounceEvent.performEvent()
        debounceEvent.performEvent()
        assertEquals(2, skippedCounter)
        Thread.sleep(50)
        debounceEvent.performEvent()
        debounceEvent.performEvent()
        assertEquals(3, skippedCounter)
    }
}