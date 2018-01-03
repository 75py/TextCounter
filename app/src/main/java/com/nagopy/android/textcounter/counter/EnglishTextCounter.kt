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

class EnglishTextCounter : TextCounter {

    val regexSpaces = "\\s".toRegex()
    val regexHyphen = "-".toRegex()
    val regexDelimiter = "[.!?\\s]+".toRegex()
    val regexPeriod = "\\.".toRegex()
    val regexMultiBr = "\n{2,}".toRegex()
    val regexBr = "\n".toRegex()
    val regexWhitespace = " ".toRegex()

    override fun countChars(inputText: String): Int {
        return inputText.remove(regexSpaces).length
    }

    override fun countWords(inputText: String): Int {
        var wkStr = inputText.replace('\n', '.')
        wkStr = wkStr.remove(regexHyphen)
        wkStr = wkStr.replace(regexDelimiter, ".")

        var result = wkStr.count(regexPeriod)

        if (wkStr.startsWith(".")) {
            result--
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith(".")) {
            result++
        }

        return result
    }

    override fun countSentences(inputText: String): Int {
        var wkStr = inputText.remove(regexSpaces)
        wkStr = wkStr.replace(regexDelimiter, ".") // 連続しているものは全て一つのピリオドに

        // ピリオドを数えることで、センテンス数を数える
        var result = wkStr.count(regexPeriod)

        if (wkStr.startsWith(".")) {
            result--
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith(".")) {
            result++
        }

        return result
    }

    override fun countParagraphs(inputText: String): Int {
        var wkStr = inputText

        // 連続した改行は一つにまとめる
        wkStr = wkStr.replace(regexMultiBr, "\n")

        // 改行をカウント
        var result = wkStr.count(regexBr)

        // 補正
        if (wkStr.startsWith("\n")) {
            result--
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith("\n")) {
            result++
        }

        return result
    }

    override fun countWhitespaces(inputText: String): Int {
        return inputText.count(regexWhitespace)
    }

}