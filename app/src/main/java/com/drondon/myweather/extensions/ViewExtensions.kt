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
 * Last modified 10/29/18 2:12 PM
 */

package com.drondon.myweather.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Provide simple access to [LayoutInflater.inflate] method
 * @param resource layout resource id
 * @param attachToRoot Whether the inflated hierarchy should be attached to
 *        the root parameter? If false, root is only used to create the
 *        correct subclass of LayoutParams for the root view in the XML.
 *        false by default
 * @return inflated [View]
 * */
fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false) =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)