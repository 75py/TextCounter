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

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.nagopy.android.textcounter.counter.TextCounters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class MainActivityTest {

    lateinit var activityController: ActivityController<MainActivity>

    @Mock
    lateinit var textCounters: TextCounters

    @Mock
    lateinit var ads: Ads

    @Mock
    lateinit var menuItem: MenuItem

    val testModule = Kodein.Module(allowSilentOverride = true) {
        bind<TextCounters>() with singleton { textCounters }
        bind<Ads>() with singleton { ads }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RuntimeEnvironment.application.asApp()
                .kodein.addImport(testModule, true)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
    }

    @Test
    fun onCreate() {
        val activity = activityController.create().get()
        assertThat(activity.textCounters, `is`(notNullValue()))
    }

    @Test
    fun onStart() {
        val activity = activityController.create().start().get()
        verify(activity.textCounters, times(1)).onStart()
        verify(activity.textCounters, never()).onStop()
        verify(activity.ads, never()).onResume()
        verify(activity.ads, never()).onPause()
        verify(activity.ads, never()).onDestroy()
    }

    @Test
    fun onResume() {
        val activity = activityController.create().start().resume().get()
        verify(activity.textCounters, times(1)).onStart()
        verify(activity.textCounters, never()).onStop()
        verify(activity.ads, times(1)).onResume()
        verify(activity.ads, never()).onPause()
        verify(activity.ads, never()).onDestroy()
    }

    @Test
    fun onPause() {
        val activity = activityController.create().start().resume().pause().get()
        verify(activity.textCounters, times(1)).onStart()
        verify(activity.textCounters, never()).onStop()
        verify(activity.ads, times(1)).onResume()
        verify(activity.ads, times(1)).onPause()
        verify(activity.ads, never()).onDestroy()
    }

    @Test
    fun onStop() {
        val activity = activityController.create().start().resume().pause().stop().get()
        verify(activity.textCounters, times(1)).onStart()
        verify(activity.textCounters, times(1)).onStop()
        verify(activity.ads, times(1)).onResume()
        verify(activity.ads, times(1)).onPause()
        verify(activity.ads, never()).onDestroy()
    }

    @Test
    fun onDestroy() {
        val activity = activityController.create().start().resume().pause().stop().destroy().get()
        verify(activity.textCounters, times(1)).onStart()
        verify(activity.textCounters, times(1)).onStop()
        verify(activity.ads, times(1)).onResume()
        verify(activity.ads, times(1)).onPause()
        verify(activity.ads, times(1)).onDestroy()
    }

    @Test
    fun onCreateOptionsMenu() {
    }

    @Test
    fun onOptionsItemSelectedLicense() {
        `when`(menuItem.itemId).thenReturn(R.id.menu_license)

        val activity = activityController.create().start().resume().get()
        activity.onOptionsItemSelected(menuItem)

        val shadowActivity = Shadows.shadowOf(activity)
        val intent = shadowActivity.peekNextStartedActivity()
        val shadowIntent = Shadows.shadowOf(intent)
        val actualClassName = shadowIntent.intentClass.name
        assertThat(actualClassName, `is`(LicenseActivity::class.java.name))
    }

}
