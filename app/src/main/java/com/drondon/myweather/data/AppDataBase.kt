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
 * Last modified 10/29/18 7:38 PM
 */

package com.drondon.myweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.drondon.myweather.Constants

@Database(entities = [CityWeather::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao

    companion object {
        /**
         * Create data base
         * */
        fun buildDatabase(context: Context, inMemoryDb: Boolean = false): AppDataBase {
            return if (inMemoryDb) {
                Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
            } else {
                Room.databaseBuilder(context, AppDataBase::class.java, Constants.DATA_BASE_NAME).build()
            }
        }
    }
}