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
 * Last modified 10/29/18 2:12 PM
 */

package com.drondon.myweather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [CityWeather] provide access to all necessary (for this application) data
 *
 * Declaring the column info allows for the renaming of variables without implementing a
 * database migration, as the column name would not change.
 * */
@Entity(tableName = "city_weather")
data class CityWeather(
    @PrimaryKey()
    @ColumnInfo(name = "city_id")
    val id: Long,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country_code")
    val country: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "temperature")
    val temperature: Double,
    @ColumnInfo(name = "modified_time")
    val modifiedTime: Long
)