package com.rivierasoft.historicalturkishseries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class WebViewActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1500*1000;

    private WebView webView;

    private Intent intent;
    private int type, id, current_season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        webView = findViewById(R.id.webView);

        intent = getIntent();

        type = intent.getIntExtra("type", 1);
        id = intent.getIntExtra("id", 1);
        current_season = intent.getIntExtra("current_season", 1);

        //webView.loadUrl("https://www.ok.ru/videoembed/2339372010099");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //String frameVideo = "<iframe width=\"560\" height=\"315\" src=\"//www.ok.ru/videoembed/2339372010099?autoplay=1\" frameborder=\"0\" allow=\"autoplay\" allowfullscreen></iframe>";

        //webView.loadData(frameVideo, "text/html", "utf-8");

        webView.loadUrl(intent.getStringExtra("video"));


        webView.setWebViewClient(new WebViewClient());

        /*webView.loadData("<iframe width=\"727\" height=\"409\" src=\"https://www.youtube.com/embed/zVvJO4-UTZ4\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>","text/html" , "utf-8");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {

        } );*/

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                if (AdsCenter.mInterstitialAd.isLoaded()) {
                    AdsCenter.mInterstitialAd.show();
                    AdsCenter.mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            //Toast.makeText(getActivity(), "ad finishes loading", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Code to be executed when an ad request fails.
                            //View parentLayout = findViewById(android.R.id.content);
                            //Snackbar.make(parentLayout, "أنت غير متصل بالإنترنت!", Snackbar.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(), "ad request fails", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                            //Toast.makeText(getActivity(), "ad is displayed", Toast.LENGTH_SHORT).show();
                            AdsCenter.loadInterstitialAd(WebViewActivity.this);
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                            //Toast.makeText(getActivity(), "user clicks on an ad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                            //Toast.makeText(getActivity(), "user has left the app", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            //Toast.makeText(getApplicationContext(), "interstitial ad is closed", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    //Toast.makeText(getApplicationContext(), "لا يوجد اتصال بالإنترنت!", Toast.LENGTH_SHORT).show();
                }
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
        switch (type) {
            case 1: startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case 2: startActivity(new Intent(getApplicationContext(), SeriesActivity.class).putExtra("id", id)
                    .putExtra("current_season", current_season));
                break;
            case 3: startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 2));
                break;
            case 4: startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 3));
                break;
        }
    }
}