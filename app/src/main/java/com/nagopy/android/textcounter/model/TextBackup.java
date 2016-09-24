package com.nagopy.android.textcounter.model;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class TextBackup {

    @Inject
    SharedPreferences sp;

    @Inject
    TextBackup() {
    }

    public void backup(String text) {
        Timber.d("backup: %s", text);
        sp.edit().putString("backup", text).apply();
    }

    public String restore() {
        Timber.d("restore");
        return sp.getString("backup", "");
    }

}
