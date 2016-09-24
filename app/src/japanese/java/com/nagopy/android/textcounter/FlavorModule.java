package com.nagopy.android.textcounter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.nagopy.android.textcounter.model.JapaneseTextCounter;
import com.nagopy.android.textcounter.model.TextCounter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FlavorModule {

    FlavorModule() {
    }

    @Singleton
    @Provides
    public TextCounter provideTextCounter() {
        return new JapaneseTextCounter();
    }
}
