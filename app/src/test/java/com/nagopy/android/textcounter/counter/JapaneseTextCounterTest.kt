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

class JapaneseTextCounterTest {

    lateinit var japaneseTextCounter: JapaneseTextCounter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        japaneseTextCounter = JapaneseTextCounter()
    }

    @Test
    fun countChars() {
        assertThat(japaneseTextCounter.countChars(""), `is`(0))
        assertThat(japaneseTextCounter.countChars("あいうえお"), `is`(5))
    }

    @Test
    fun countWords() {
        assertThat(japaneseTextCounter.countWords(""), `is`(-1))
        assertThat(japaneseTextCounter.countWords("あいうえお"), `is`(-1))
    }

    @Test
    fun countSentences() {
        assertThat(japaneseTextCounter.countSentences(""), `is`(0))
        assertThat(japaneseTextCounter.countSentences("眠い"), `is`(1))
        assertThat(japaneseTextCounter.countSentences("眠い。"), `is`(1))
        assertThat(japaneseTextCounter.countSentences("眠い。とても眠い。"), `is`(2))
    }

    @Test
    fun countParagraphs() {
        assertThat(japaneseTextCounter.countParagraphs(""), `is`(0))
        assertThat(japaneseTextCounter.countParagraphs("眠い。"), `is`(1))
        assertThat(japaneseTextCounter.countParagraphs("眠い。\nとても眠い。"), `is`(2))
        assertThat(japaneseTextCounter.countParagraphs("眠い。とても眠い。"), `is`(1))
    }

    @Test
    fun countWhitespaces() {
        assertThat(japaneseTextCounter.countWhitespaces(""), `is`(0))
        assertThat(japaneseTextCounter.countWhitespaces("眠い。"), `is`(0))
        assertThat(japaneseTextCounter.countWhitespaces("眠い。\nとても眠い。"), `is`(0))
        assertThat(japaneseTextCounter.countWhitespaces("眠い。　とても眠い。"), `is`(1))
        assertThat(japaneseTextCounter.countWhitespaces("眠い。　とても眠い。 とてつもなく眠い。"), `is`(2))
    }

}