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
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.nagopy.android.textcounter.counter.TextCounters
import com.nagopy.android.textcounter.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : KodeinAppCompatActivity()
        , Ads.RewardAdsExplanationDialogFragment.Listener
        , TextCounters.TextCounterLanguageSelectDialogFragment.Listener {

    lateinit var binding: ActivityMainBinding

    val textCounters: TextCounters by instance()
    val ads: Ads by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.textCounter = textCounters

        ads.onCreate(this, binding.ad)
    }

    override fun onStart() {
        super.onStart()
        textCounters.onStart()
    }

    override fun onResume() {
        super.onResume()
        ads.onResume()
    }

    override fun onPause() {
        ads.onPause()
        super.onPause()
    }

    override fun onStop() {
        textCounters.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        ads.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Timber.d("onCreateOptionsMenu")
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Timber.d("onPrepareOptionsMenu() ads.shouldDisplayBannerAds()=%s", ads.shouldDisplayBannerAds())
        menu?.findItem(R.id.menu_reward)?.isVisible = ads.shouldDisplayBannerAds()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Timber.d("onOptionsItemSelected item?.itemId=%d", item?.itemId)
        when (item?.itemId) {
            R.id.menu_license -> {
                startActivity(Intent(this, LicenseActivity::class.java))
            }
            R.id.menu_share_text -> {
                ShareCompat.IntentBuilder.from(this)
                        .setSubject(getString(R.string.share_text_subject))
                        .setText(binding.editText.text.toString())
                        .setType("text/plain")
                        .startChooser()
            }
            R.id.menu_share_result -> {
                val resultStr = listOf(
                        binding.resultChars
                        , binding.resultWords
                        , binding.resultSentences
                        , binding.resultParagraphs
                        , binding.resultWhitespaces
                ).filter { it.visibility == View.VISIBLE }
                        .joinToString(separator = "\n") { it.text }
                ShareCompat.IntentBuilder.from(this)
                        .setSubject(getString(R.string.share_result_subject))
                        .setText(resultStr)
                        .setType("text/plain")
                        .startChooser()
            }
            R.id.menu_reward -> {
                if (ads.shouldDisplayBannerAds()) {
                    Ads.RewardAdsExplanationDialogFragment
                            .newInstance()
                            .show(supportFragmentManager, "RewardAdsExplanationDialogFragment")
                }
            }
            R.id.menu_choose_count_lang -> {
                TextCounters.TextCounterLanguageSelectDialogFragment
                        .newInstance()
                        .show(supportFragmentManager, "TextCounterLanguageSelectDialogFragment")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRewardAdsStartClicked() {
        Timber.d("onRewardAdsStartClicked")
        ads.showRewardAds()
    }

    override fun onTextCounterLanguageChanged() {
        Timber.d("onTextCounterLanguageChanged")
        invalidateOptionsMenu()
        supportInvalidateOptionsMenu()
        textCounters.counter = textCounters.getCurrentTextCounter()
        textCounters.update()
        updateTitle()
    }

    fun updateTitle() {
        title = getString(R.string.app_name_with_count_lang,
                resources.getStringArray(R.array.count_lang)[textCounters.getCurrentLangIndex()]
        )
    }
}
