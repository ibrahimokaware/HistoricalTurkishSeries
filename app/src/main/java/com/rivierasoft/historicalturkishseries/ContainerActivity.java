package com.rivierasoft.historicalturkishseries;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Locale;

public class ContainerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        Configuration newConfig = new Configuration( res.getConfiguration() );
        Locale locale = new Locale( "ar" );
        newConfig.locale = locale;
        newConfig.setLocale(locale);
        newConfig.setLayoutDirection( locale );
        res.updateConfiguration( newConfig, null );
        setContentView(R.layout.activity_container);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        intent = getIntent();
        switch (intent.getIntExtra("fragment", 1)) {
            case 1:
                getSupportActionBar().setTitle(getString(R.string.all_series));
                SeriesFragment seriesFragment = new SeriesFragment();
                fragmentTransaction.replace(R.id.fragment_container, seriesFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 2:
                getSupportActionBar().setTitle(getString(R.string.latest));
                LatestEpisodesFragment latestEpisodesFragment = new LatestEpisodesFragment();
                fragmentTransaction.replace(R.id.fragment_container, latestEpisodesFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 3:
                getSupportActionBar().setTitle(getString(R.string.most));
                MostWatchedFragment mostWatchedFragment = new MostWatchedFragment();
                fragmentTransaction.replace(R.id.fragment_container, mostWatchedFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}