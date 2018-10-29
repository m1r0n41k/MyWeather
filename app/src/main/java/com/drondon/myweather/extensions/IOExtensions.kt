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
 * Last modified 10/28/18 4:09 PM
 */

package com.drondon.myweather.extensions

private const val _KB = 1024
private const val _MB = 1024 * _KB
private const val _GB = 1024 * _MB

/**
 * Convert receiver value of MB to bytes
 *
 * @return bytes count
 */
val Int.MB
    get() = this * _MB

/**
 * Convert receiver value of MB to bytes
 *
 * @return bytes count
 */
val Long.MB
    get() = this * _MB