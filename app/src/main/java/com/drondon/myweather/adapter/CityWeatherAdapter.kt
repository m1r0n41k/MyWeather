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
 * Last modified 10/29/18 7:19 PM
 */

package com.drondon.myweather.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drondon.myweather.R
import com.drondon.myweather.common.ImageLoader
import com.drondon.myweather.common.WeatherIconSource
import com.drondon.myweather.data.CityWeather
import com.drondon.myweather.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_city_weather.*
import java.text.DecimalFormat

class CityWeatherAdapter(private val imageLoader: ImageLoader) :
    ListAdapter<CityWeather, CityWeatherAdapter.CityWeatherViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder {
        return CityWeatherViewHolder(parent.inflate(R.layout.item_city_weather))
    }

    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {
        val cityWeather = getItem(position)
        holder.bind(cityWeather)
        imageLoader.load(WeatherIconSource(cityWeather.icon), holder.iconImageView)
    }

    class CityWeatherViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        @SuppressLint("SetTextI18n")
        fun bind(cityWeather: CityWeather) {
            nameTextView.text = "${cityWeather.name},${cityWeather.country.toUpperCase()}"
            val temp = cityWeather.temperature
            val result = temp.compareTo(0.0)
            val symbol = when {
                result > 0 -> "+"
                result < 0 -> "-"
                else -> ""
            }
            temperatureTextView.text = "$symbol${temperatureFormatter.format(temp)}"
            descriptionTextView.text = cityWeather.description
        }
    }

    companion object {

        private val temperatureFormatter = DecimalFormat("###°")

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CityWeather>() {
            override fun areItemsTheSame(oldItem: CityWeather, newItem: CityWeather) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
                return newItem.modifiedTime == oldItem.modifiedTime
                        && newItem.temperature == oldItem.temperature
                        && newItem.description == oldItem.description
                        && newItem.icon == oldItem.icon
                        && newItem.name == oldItem.name
            }
        }
    }
}