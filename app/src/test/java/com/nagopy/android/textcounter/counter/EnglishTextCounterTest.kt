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

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class EnglishTextCounterTest {

    lateinit var englishTextCounter: EnglishTextCounter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        englishTextCounter = EnglishTextCounter()
    }

    @Test
    fun countChars() {
        assertThat(englishTextCounter.countChars(""), `is`(0))
        assertThat(englishTextCounter.countChars("I'm sleepy."), `is`(10))
    }

    @Test
    fun countWords() {
        assertThat(englishTextCounter.countWords(""), `is`(0))
        assertThat(englishTextCounter.countWords("I'm sleepy."), `is`(2))
        assertThat(englishTextCounter.countWords("I'm sleepy ."), `is`(2))
    }

    @Test
    fun countSentences() {
        assertThat(englishTextCounter.countSentences(""), `is`(0))
        assertThat(englishTextCounter.countSentences("I'm sleepy."), `is`(1))
        assertThat(englishTextCounter.countSentences("I'm sleepy"), `is`(1))
        assertThat(englishTextCounter.countSentences("I'm sleepy. I'm sleepy."), `is`(2))
    }

    @Test
    fun countParagraphs() {
        assertThat(englishTextCounter.countParagraphs(""), `is`(0))
        assertThat(englishTextCounter.countParagraphs("I'm sleepy."), `is`(1))
        assertThat(englishTextCounter.countParagraphs("I'm sleepy. \nI'm sleepy."), `is`(2))
        assertThat(englishTextCounter.countParagraphs("I'm sleepy. I'm sleepy."), `is`(1))
    }

    @Test
    fun countWhitespaces() {
        assertThat(englishTextCounter.countWhitespaces(""), `is`(0))
        assertThat(englishTextCounter.countWhitespaces("I'm sleepy."), `is`(1))
        assertThat(englishTextCounter.countWhitespaces("I'm sleepy.\nI'm sleepy."), `is`(2))
        assertThat(englishTextCounter.countWhitespaces("I'm sleepy. I'm sleepy."), `is`(3))
    }

}