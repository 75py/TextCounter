package com.nagopy.android.textcounter.vm;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;

import com.nagopy.android.textcounter.model.TextBackup;
import com.nagopy.android.textcounter.model.TextCounter;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class VMTextCounter {

    public ObservableField<String> text = new ObservableField<>();
    public ObservableField<Integer> bytes = new ObservableField<>(0);
    public ObservableField<Integer> chars = new ObservableField<>(0);
    public ObservableField<Integer> words = new ObservableField<>(0);
    public ObservableField<Integer> sentences = new ObservableField<>(0);
    public ObservableField<Integer> paragraphs = new ObservableField<>(0);

    @Inject
    TextCounter textCounter;

    @Inject
    TextBackup textBackup;

    @Inject
    VMTextCounter() {
    }

    public void onStart() {
        Timber.d("onStart");
        text.addOnPropertyChangedCallback(callback);
        text.set(textBackup.restore());
    }

    public void onStop() {
        Timber.d("onStop");
        text.removeOnPropertyChangedCallback(callback);
        textBackup.backup(text.get());
    }

    private Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            Timber.d("onPropertyChanged");
            textCounter.update(VMTextCounter.this);
        }
    };

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Timber.d("afterTextChanged %s, current=%s", s, text.get());
            if (!objEquals(text.get(), s.toString())) {
                text.set(s.toString());
            }
        }
    };

    static boolean objEquals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
