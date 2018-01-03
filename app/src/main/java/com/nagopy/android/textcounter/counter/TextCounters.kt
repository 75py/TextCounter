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

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import com.nagopy.android.textcounter.R
import com.nagopy.android.textcounter.TextBackup
import com.nagopy.android.textcounter.counter.TextCounters.TextCounterLanguageSelectDialogFragment.Companion.KEY_LANG
import timber.log.Timber
import java.util.*

class TextCounters(
        val sharedPreferences: SharedPreferences
        , val textBackup: TextBackup
        , val englishTextCounter: TextCounter
        , val japaneseTextCounter: TextCounter) {

    var counter = getCurrentTextCounter()

    fun getCurrentLangIndex(): Int {
        return if (sharedPreferences.contains(KEY_LANG)) {
            return sharedPreferences.getInt(KEY_LANG, 0)
        } else {
            when (Locale.getDefault().language) {
                "ja" -> 1
                else -> 0
            }
        }
    }

    fun getCurrentTextCounter(): TextCounter {
        return when (getCurrentLangIndex()) {
            0 -> englishTextCounter
            1 -> japaneseTextCounter
            else -> englishTextCounter
        }
    }

    class TextCounterLanguageSelectDialogFragment : DialogFragment() {

        lateinit var listener: Listener

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            listener = activity as Listener
        }

        @SuppressLint("ApplySharedPref")
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity!!)
                    .setTitle(R.string.choose_count_lang)
                    .setItems(R.array.count_lang) { _, which ->
                        PreferenceManager.getDefaultSharedPreferences(activity)
                                .edit()
                                .putInt(KEY_LANG, which)
                                .commit()
                        listener.onTextCounterLanguageChanged()
                    }
                    .create()
        }

        interface Listener {
            fun onTextCounterLanguageChanged()
        }

        companion object {
            fun newInstance() = TextCounterLanguageSelectDialogFragment()
            val KEY_LANG = "countLang"
        }
    }


    val text = ObservableField<String>()
    val chars = ObservableField(0)
    val words = ObservableField(0)
    val sentences = ObservableField(0)
    val paragraphs = ObservableField(0)
    val whitespaces = ObservableField(0)
    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, i: Int) {
            Timber.d("onPropertyChanged")
            update()
        }
    }
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            Timber.d("afterTextChanged %s, current=%s", s, text.get())
            if (text.get() != s.toString()) {
                text.set(s.toString())
            }
        }
    }

    fun onStart() {
        Timber.d("onStart")
        text.addOnPropertyChangedCallback(callback)
        text.set(textBackup.restore())
    }

    fun onStop() {
        Timber.d("onStop")
        text.removeOnPropertyChangedCallback(callback)
        textBackup.backup(text.get())
    }

    fun update() {
        var inputText = text.get()
        if (inputText.isNullOrEmpty()) {
            inputText = ""
        }

        chars.set(counter.countChars(inputText))
        words.set(counter.countWords(inputText))
        sentences.set(counter.countSentences(inputText))
        paragraphs.set(counter.countParagraphs(inputText))
        whitespaces.set(counter.countWhitespaces(inputText))
    }

}
