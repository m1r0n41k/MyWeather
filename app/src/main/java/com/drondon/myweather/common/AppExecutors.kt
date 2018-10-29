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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:44 AM
 *
 * Last modified 10/29/18 1:11 AM
 */

package com.drondon.myweather.common

interface AppExecutors {
    /**
     * Utility method to run blocks on a dedicated background thread, used for io/database work.
     */
    fun runOnIoThread(f: () -> Unit)

    /**
     * Utility method to run blocks on a UI thread.
     */
    fun runOnUiThread(f: () -> Unit)
}