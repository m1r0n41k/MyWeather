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
 * Created by  - Andrew Miroshnychenko on 10/30/18 12:45 AM
 *
 * Last modified 10/29/18 11:46 PM
 */

package com.drondon.myweather.common

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import timber.log.Timber

interface ImageLoader {
    fun load(image: ImageSource, into: Any?)
}

class FragmentImageLoader(private val fragment: Fragment) : ImageLoader {
    override fun load(image: ImageSource, into: Any?) {
        into as ImageView
        Glide.with(fragment).load(image.getSource()).into(into)
    }
}

class ContextImageLoader(private val context: Context) : ImageLoader {
    override fun load(image: ImageSource, into: Any?) {
        Glide.with(context).downloadOnly().load(image.getSource()).submit(50, 50)
    }
}

class TestImageLoader() : ImageLoader {
    override fun load(image: ImageSource, into: Any?) {
        Timber.d("TestImageLoader: %s", image.getSource())
    }
}