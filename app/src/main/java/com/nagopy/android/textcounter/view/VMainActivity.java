package com.nagopy.android.textcounter.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.nagopy.android.textcounter.App;
import com.nagopy.android.textcounter.R;
import com.nagopy.android.textcounter.databinding.ActivityMainBinding;
import com.nagopy.android.textcounter.vm.VMTextCounter;

import javax.inject.Inject;

public class VMainActivity extends AppCompatActivity {

    @Inject
    VMTextCounter vmTextCounter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).applicationComponent.inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(vmTextCounter);

        binding.ad.loadAd(new AdRequest.Builder()
                .addTestDevice("4EB260715A6D70807B32DAAC473002C9")
                .addTestDevice("367CE774B209A0E99A3D1674F2404408")
                .build()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        vmTextCounter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.ad.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.ad.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vmTextCounter.onStop();
    }

    @Override
    protected void onDestroy() {
        binding.ad.destroy();
        super.onDestroy();
    }

    public void startLicenseActivity(MenuItem menuItem) {
        startActivity(new Intent(this, VLicenseActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
}
