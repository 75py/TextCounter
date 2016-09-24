package com.nagopy.android.textcounter.model;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnglishTextCounter extends TextCounter {

    Pattern regexSpaces = Pattern.compile("\\s");
    Pattern regexMultiBr = Pattern.compile("\n{2,}");
    Pattern regex = Pattern.compile("[\\.!\\?\\s]+");

    @Inject
    public EnglishTextCounter() {
    }

    @Override
    int countChars(@NonNull String text) {
        return remove(text, regexSpaces).length();
    }

    @Override
    int countWords(@NonNull String text) {
        String wkStr = text.replace('\n', '.');
        wkStr = remove(wkStr, "-");
        wkStr = replace(wkStr, regex, ".");

        int result = count(wkStr, ".");

        if (wkStr.startsWith(".")) {
            result--;
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith(".")) {
            result++;
        }

        return result;
    }

    @Override
    int countSentences(@NonNull String text) {
        String wkStr = remove(text, regexSpaces);
        wkStr = replace(wkStr, regex, "."); // 連続しているものは全て一つのピリオドに

        // ピリオドを数えることで、センテンス数を数える
        int result = count(wkStr, ".");

        // 補正
        if (wkStr.startsWith(".")) {
            result--;
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith(".")) {
            result++;
        }

        return result;
    }

    @Override
    int countParagraphs(@NonNull String text) {
        String wkStr = text;
/*
        // 文字列が改行のみの場合はカウントを中止
        String wkStr = remove(text, regexSpaces);
        if (wkStr.isEmpty()) {
            return 0;
        }
*/

        // 連続した改行は一つにまとめる
        wkStr = replace(wkStr, regexMultiBr, "\n");

        // 改行をカウント
        int result = count(wkStr, "\n");

        // 補正
        if (wkStr.startsWith("\n")) {
            result--;
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith("\n")) {
            result++;
        }

        return result;
    }
}
