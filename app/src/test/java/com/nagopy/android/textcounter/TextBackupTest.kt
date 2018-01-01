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

package com.nagopy.android.textcounter

import android.preference.PreferenceManager
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class TextBackupTest {

    lateinit var textBackup: TextBackup

    @Before
    fun setUp() {
        val sp = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
        sp.edit().clear().commit()
        textBackup = TextBackup(sp)
    }

    @Test
    fun backupAndRestore() {
        assertThat(textBackup.restore(), CoreMatchers.`is`(""))
        textBackup.backup("test")
        assertThat(textBackup.restore(), CoreMatchers.`is`("test"))
    }

}