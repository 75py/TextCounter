/*
 * Copyright 2018 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nagopy.android.textcounter.counter

interface TextCounter {

    fun countChars(inputText: String): Int
    fun countWords(inputText: String): Int
    fun countSentences(inputText: String): Int
    fun countParagraphs(inputText: String): Int
    fun countWhitespaces(inputText: String): Int

    fun String.remove(regex: Regex): String = this.replace(regex, "")
    fun String.count(regex: Regex): Int = this.length - remove(regex).length
}