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
 * Last modified 10/29/18 11:30 PM
 */

package com.drondon.myweather.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.drondon.myweather.R
import com.drondon.myweather.adapter.CityWeatherAdapter
import com.drondon.myweather.common.ImageLoader
import com.drondon.myweather.di.DI
import com.drondon.myweather.utils.DebounceEvent
import com.drondon.myweather.view.StatusBar
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.ParameterList
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val dateFormatter by lazy { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    private val viewModel: MainViewModel by sharedViewModel()

    private val imageLoader: ImageLoader by inject(DI.IMAGE_LOADER_FRAGMENT) { ParameterList(this) }

    private val refreshDebounced = object : DebounceEvent(20000) {
        override fun onDebouncedEvent() {
            //Refresh data if user launch app
            viewModel.refresh()
        }

        override fun onSkippedEvent() {
            view?.postDelayed({
                viewModel.hideProgress()
            }, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            //Initial request for populate data
            refreshDebounced.performEvent()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare city list
        val adapter = CityWeatherAdapter(imageLoader)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        swipeRefreshLayout.setOnRefreshListener(refreshDebounced::performEvent)

        // Observe data
        viewModel.cityWeather.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.progress.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = it
        })

        viewModel.networkState.observe(this, Observer {
            if (it.first == null || it.first?.isConnected == true) {
                refreshDebounced.performEvent()
                StatusBar(view, containerFooter).hide()
            } else {
                val lastModified = TimeUnit.SECONDS.toMillis(it.second)
                StatusBar(view, containerFooter).show(
                    "Offline mode. This weather was actual at ${dateFormatter.format(Date(lastModified))}!"
                )
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> refreshDebounced.performEvent()
        }
        return super.onOptionsItemSelected(item)
    }
}
