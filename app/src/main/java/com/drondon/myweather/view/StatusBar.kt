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
 * Created by  - Andrew Miroshnychenko on 10/30/18 11:45 PM
 *
 * Last modified 10/30/18 11:45 PM
 */

package com.drondon.myweather.view

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.drondon.myweather.R
import com.drondon.myweather.extensions.inflate

/**
 * Simple utility class for show [R.layout.view_status]
 *
 * @constructor wrap root view and container
 * @param rootView parent container for animations with [TransitionManager]
 * @param container container for show status
 * */
class StatusBar(private val rootView: View, val container: ViewGroup) {

    /**
     * Show custom status view on top of the screen or update message if it is showing
     *
     * @param message text message
     * */
    fun show(message: CharSequence) {
        if (container.childCount == 0) {
            TransitionManager.beginDelayedTransition(rootView as ViewGroup)
            val statusView = container.inflate(R.layout.view_status)
            container.addView(statusView)
        }
        val statusTextView = rootView.findViewById<TextView>(R.id.offlineStatusTextView)
        statusTextView.text = message
    }

    /**
     * Hide status view
     * */
    fun hide() {
        if (container.childCount > 0) {
            val statusView = container.getChildAt(0)
            TransitionManager.beginDelayedTransition(rootView as ViewGroup)
            container.removeView(statusView)
        }
    }
}