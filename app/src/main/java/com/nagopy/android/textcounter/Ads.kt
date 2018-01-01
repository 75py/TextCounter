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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import timber.log.Timber
import java.lang.ref.WeakReference

class Ads(val sharedPreferences: SharedPreferences) {

    lateinit var activity: WeakReference<Activity?>
    var bannerAdView: AdView? = null
    var rewardedVideoAd: RewardedVideoAd? = null

    fun onCreate(activity: Activity, adView: AdView) {
        this.activity = WeakReference(activity)
        if (shouldDisplayBannerAds()) {
            val adRequest = AdRequest.Builder()
                    .addTestDevice("2DB1FAD5A2B662EC730051B92B0C0AB7")
                    .build()
            adView.loadAd(adRequest)
            this.bannerAdView = adView
        } else {
            adView.visibility = View.GONE
            this.bannerAdView = null
        }
    }

    fun onResume() {
        bannerAdView?.resume()
        rewardedVideoAd?.resume(activity.get())
    }

    fun onPause() {
        bannerAdView?.pause()
        rewardedVideoAd?.pause(activity.get())
    }

    fun onDestroy() {
        bannerAdView?.destroy()
        rewardedVideoAd?.destroy(activity.get())
    }

    fun shouldDisplayBannerAds(): Boolean {
        val limitMs = sharedPreferences.getLong(KEY_BANNER_ADS_HIDE_LIMIT, 0)
        return limitMs < System.currentTimeMillis()
    }

    class RewardAdsExplanationDialogFragment : DialogFragment() {

        lateinit var listener: Listener
        override fun onAttach(context: Context?) {
            super.onAttach(context)
            listener = activity as Listener
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity!!)
                    .setTitle(R.string.reward_explain_title)
                    .setMessage(R.string.reward_explain_text)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        listener.onRewardAdsStartClicked()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .create()
        }

        interface Listener {
            fun onRewardAdsStartClicked()
        }

        companion object {
            fun newInstance() = RewardAdsExplanationDialogFragment()

        }
    }

    fun showRewardAds() {
        val act = activity.get()
        if (act != null) {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(act)
            rewardedVideoAd!!.rewardedVideoAdListener = rewardedVideoAdListener

            rewardedVideoAd!!.loadAd(act.getString(R.string.reward_ad_unit_id),
                    AdRequest.Builder().build())
        }
    }

    val rewardedVideoAdListener = object : RewardedVideoAdListener {
        override fun onRewardedVideoAdClosed() {
            Timber.d("onRewardedVideoAdClosed")
        }

        override fun onRewardedVideoAdLeftApplication() {
            Timber.d("onRewardedVideoAdLeftApplication")
        }

        override fun onRewardedVideoAdLoaded() {
            Timber.d("onRewardedVideoAdLoaded")
            rewardedVideoAd?.show()
        }

        override fun onRewardedVideoAdOpened() {
            Timber.d("onRewardedVideoAdOpened")
        }

        @SuppressLint("ApplySharedPref")
        override fun onRewarded(reward: RewardItem?) {
            Timber.d("onRewarded amount=%d, type=%s", reward?.amount, reward?.type)
            val newLimitMs = System.currentTimeMillis() + ((reward?.amount ?: 0) * 60 * 60 * 1000)
            sharedPreferences.edit()
                    .putLong(KEY_BANNER_ADS_HIDE_LIMIT, newLimitMs)
                    .commit()

            bannerAdView?.apply {
                pause()
                destroy()
                visibility = View.GONE
            }
            bannerAdView = null
        }

        override fun onRewardedVideoStarted() {
            Timber.d("onRewardedVideoStarted")
        }

        override fun onRewardedVideoAdFailedToLoad(p0: Int) {
            Timber.d("onRewardedVideoAdFailedToLoad %d", p0)
        }
    }

    companion object {
        val KEY_BANNER_ADS_HIDE_LIMIT = "bannerAdsHideLimit"
    }
}
