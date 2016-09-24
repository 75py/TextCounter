package com.nagopy.android.textcounter.model;


import android.support.annotation.NonNull;

import com.nagopy.android.textcounter.vm.VMTextCounter;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

public abstract class TextCounter {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    TextCounter() {
    }

    public void update(VMTextCounter vm) {
        String text = vm.text.get();
        if (text == null) {
            text = "";
        }

        vm.bytes.set(countBytes(text));
        vm.chars.set(countChars(text));
        vm.words.set(countWords(text));
        vm.sentences.set(countSentences(text));
        vm.paragraphs.set(countParagraphs(text));
    }

    int countBytes(@NonNull String text) {
        return text.getBytes(UTF_8).length;
    }

    abstract int countChars(@NonNull String text);

    abstract int countWords(@NonNull String text);

    abstract int countSentences(@NonNull String text);

    abstract int countParagraphs(@NonNull String text);


    String remove(String text, String target) {
        return text.replace(target, "");
    }

    String remove(String text, Pattern p) {
        return p.matcher(text).replaceAll("");
    }

    String replace(String text, Pattern p, String replacement) {
        return p.matcher(text).replaceAll(replacement);
    }

    int count(String text, String target) {
        return text.length() - remove(text, target).length();
    }

}
