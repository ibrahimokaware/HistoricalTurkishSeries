package com.rivierasoft.historicalturkishseries;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PhotoSlidePagerActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 180*1000;

    private Intent intent;

    private ProgressBar progressBar;
    private SeekBar seekBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference seriesReference = db.collection("Series");

    private ArrayList<String> imageUrls;

    private TextView textView;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

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
        setContentView(R.layout.activity_photo_slide_pager);

        intent = getIntent();

        textView = findViewById(R.id.textView);
        viewPager = findViewById(R.id.pager);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);

        textView.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

        readData((list) -> {

            seekBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            //downloadIV.setVisibility(View.VISIBLE);
            //swapIV.setVisibility(View.VISIBLE);

            textView.setText(1+"/"+imageUrls.size());
            seekBar.setMax(imageUrls.size());

            //viewPager.setPageTransformer(new ZoomOutPageTransformer());
            pagerAdapter = new ScreenSlidePagerAdapter(this);
            viewPager.setAdapter(pagerAdapter);

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    textView.setText(position+1+"/"+imageUrls.size());
                    seekBar.setProgress(position+1);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    viewPager.setCurrentItem(progress - 1);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

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
                                AdsCenter.loadInterstitialAd(PhotoSlidePagerActivity.this);
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

            // download code
//                String url = imageUrls.get(viewPager.getCurrentItem());
//                String sub = url.substring(url.lastIndexOf("."));
//                String[] e = sub.split("[?]");
//                String ext = e[0];
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                String name = intent.getIntExtra("id", 1)+"-"+(viewPager.getCurrentItem()+1);
//                request.setTitle(name);
//                request.setDescription("التنزيل");
//// in order for this if to run, you must use the android 3.2 to compile your app
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    request.allowScanningByMediaScanner();
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                }
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+ext);
//
//// get download service and enqueue file
//                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                manager.enqueue(request);

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }*/
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            //textView.setText(position+1+"/"+imageUrls.size());
            return PhotoSlideFragment.newInstance(imageUrls.get(position), position+1+"/"+imageUrls.size());
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }
    }

    public void readData(final MyCallback myCallback) {
        seriesReference
                .document(intent.getIntExtra("id", 1)+"")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                imageUrls = (ArrayList<String>) document.get("photo_gallery");
                            } else {

                            }
                            progressBar.setVisibility(View.GONE);
                            myCallback.onCallback(imageUrls);
                        } else {
                            Toast.makeText(PhotoSlidePagerActivity.this.getApplicationContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public interface MyCallback {
        void onCallback(ArrayList<String> list);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }
}