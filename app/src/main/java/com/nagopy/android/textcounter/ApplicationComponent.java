package com.nagopy.android.textcounter;

import com.nagopy.android.textcounter.view.VMainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, FlavorModule.class})
public interface ApplicationComponent {

    void inject(VMainActivity VMainActivity);

}
