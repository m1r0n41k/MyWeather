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

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val IO_EXECUTOR = Executors.newSingleThreadExecutor()
private val UI_HANDLER = Handler(Looper.getMainLooper())
val UI_EXECUTOR = Executor { command ->
    command?.apply {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            UI_HANDLER.post(this)
        } else {
            run()
        }
    }
}

class AppExecutorsImpl(private val ioExecutor: Executor, private val uiExecutor: Executor) : AppExecutors {
    override fun runOnIoThread(f: () -> Unit) {
        ioExecutor.execute(f)
    }

    override fun runOnUiThread(f: () -> Unit) {
        uiExecutor.execute(f)
    }
}