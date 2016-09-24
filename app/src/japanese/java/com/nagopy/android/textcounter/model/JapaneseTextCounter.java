package com.nagopy.android.textcounter.model;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JapaneseTextCounter extends TextCounter {

    Pattern regexMultiBr = Pattern.compile("\n{2,}");
    Pattern regex = Pattern.compile("[\\.!\\?\\s]+");
    Pattern regexMultiMaru = Pattern.compile("。{2,}");

    @Inject
    public JapaneseTextCounter() {
    }

    @Override
    int countChars(@NonNull String text) {
        return text.length();
    }

    @Override
    int countWords(@NonNull String text) {
        // unsupported
        return -1;
    }

    @Override
    int countSentences(@NonNull String text) {
        String wkStr = text.trim();
        wkStr = replace(wkStr, regex, "。");
        wkStr = replace(wkStr, regexMultiMaru, " 。");

        int result = count(wkStr, "。");

        if (wkStr.startsWith("。")) {
            result--;
        }

        if (!wkStr.isEmpty() && !wkStr.endsWith("。")) {
            result++;
        }

        return result;
    }

    @Override
    int countParagraphs(@NonNull String text) {
        String wkStr = text.trim();
        wkStr = replace(wkStr, regexMultiBr, "\n");

        int result = count(wkStr, "\n");

        if (!wkStr.isEmpty() && !wkStr.endsWith("\n")) {
            result++;
        }

        return result;
    }
}
