package com.rivierasoft.historicalturkishseries;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SeriesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int id, no_of_seasons, current_season;
    private boolean isEarn;

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private ImageView mainImageView;
    private TextView aboutTextView, seasonTextView, episodesTextView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference seriesReference = db.collection("Series");
    private CollectionReference episodesReference = db.collection("Episodes");

    private ArrayList<Episode> episodeArrayList;
    private List<Episode> episodeList = new ArrayList<>();
    private ArrayList<Series> seriesArrayList;
    private List<Series> seriesList = new ArrayList<>();
    private List<Integer> e_of_each_season = new ArrayList<>();

    private List<String> seasons = new ArrayList<>();

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
        setContentView(R.layout.activity_series);

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        sharedPreferences = getSharedPreferences("views" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        intent = getIntent();
        id = intent.getIntExtra("id", 1);
        current_season = intent.getIntExtra("current_season", 1);

        for (int i=1; i<=7; i++)
            seasons.add("الموسم "+i);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linearLayout);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        progressBar = findViewById(R.id.progressBar);
        mainImageView = findViewById(R.id.title_imageView);
        aboutTextView = findViewById(R.id.textView4);
        seasonTextView = findViewById(R.id.textView5);
        episodesTextView = findViewById(R.id.textView6);

        appBarLayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.GONE);


        readData((list, list2) -> {
            seriesArrayList = (ArrayList<Series>) list;
            Series series = seriesArrayList.get(0);

            episodeArrayList = (ArrayList<Episode>) list2;

            appBarLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);

            setSupportActionBar(toolbar);

            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(series.getPicture())
                                    .into(mainImageView);

            toolbar.setTitle(series.getName());
            aboutTextView.setText(series.getAbout());
            e_of_each_season = series.getE_of_each_s();

            no_of_seasons = series.getNo_of_seasons();

            episodesTextView.setText("الحلقات ("+e_of_each_season.get(current_season-1)+")");
            seasonTextView.setText(seasons.get(current_season-1));

            linearLayout.setOnClickListener(v -> {
                if (no_of_seasons == 1) {
                    Toast.makeText(getApplicationContext(), "متوفر موسم واحد فقط", Toast.LENGTH_SHORT).show();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SeasonsFragment seasonsFragment = SeasonsFragment.newInstance(id, no_of_seasons, current_season);
                    fragmentTransaction.add(android.R.id.content, seasonsFragment);
                    //fragmentTransaction.replace(R.id.fragment_container, createFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            EpisodeAdapter episodeAdapter = new EpisodeAdapter(episodeArrayList, R.layout.episode, getApplicationContext(),
                    p -> {
                        if (AdsCenter.rewardedAd.isLoaded()) {
                            //r = r+1;
                            Activity activityContext = SeriesActivity.this;
                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                @Override
                                public void onRewardedAdOpened() {
                                    // Ad opened.
                                    //Toast.makeText(getActivity(), "Ad opened.", Toast.LENGTH_SHORT).show();
                                    AdsCenter.loadRewardedAd(activityContext);
                                }

                                @Override
                                public void onRewardedAdClosed() {
                                    // Ad closed.
                                    if (!isEarn)
                                        Toast.makeText(getApplicationContext(), "قم بمشاهدة الإعلان كاملاً لبدء الحلقة.", Toast.LENGTH_SHORT).show();
                                    else {
                                        finish();
                                        String document = episodeArrayList.get(p).getDocument();
                                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                                .putExtra("id", id).putExtra("current_season", current_season).putExtra("type", 2));
                                        if (sharedPreferences.getInt(document, 0) == 0) {
                                            editor.putInt(document, 1);
                                            editor.apply();
                                            episodesReference
                                                    .document(episodeArrayList.get(p).getDocument())
                                                    .update("views", episodeArrayList.get(p).getViews()+1)
                                                    .addOnSuccessListener(aVoid -> {
                                                        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem reward) {
                                    // User earned reward.
                                    isEarn = true;
                                }

                                @Override
                                public void onRewardedAdFailedToShow(AdError adError) {
                                    // Ad failed to display.
                                    Toast.makeText(getApplicationContext(), "Ad failed to display.", Toast.LENGTH_SHORT).show();
                                }
                            };
                            AdsCenter.rewardedAd.show(activityContext, adCallback);
                        } else {
                            finish();
                            String document = episodeArrayList.get(p).getDocument();
                            startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                    .putExtra("id", id).putExtra("current_season", current_season).putExtra("type", 2));
                            if (sharedPreferences.getInt(document, 0) == 0) {
                                editor.putInt(document, 1);
                                editor.apply();
                                episodesReference
                                        .document(episodeArrayList.get(p).getDocument())
                                        .update("views", episodeArrayList.get(p).getViews()+1)
                                        .addOnSuccessListener(aVoid -> {
                                            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(episodeAdapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.series_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(getApplicationContext(), PhotoSlidePagerActivity.class).putExtra("id", id));
                break;
            case R.id.item2:
                finish();
                break;
        }
        return true;
    }

    public void readData(final MyCallback myCallback) {
        seriesReference
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            seriesList.add(new Series(Integer.parseInt(document.get("id").toString()), document.getString("name"),
                                    document.getString("main_picture2"), document.getString("about"), null,
                                    null, Integer.parseInt(document.get("no_of_seasons").toString()),
                                    (ArrayList<Integer>) document.get("e_of_each_s"), null, true));
                        } episodesReference
                                .whereEqualTo("series_id", id)
                                .whereEqualTo("season", current_season)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task2.getResult()) {
                                            progressBar.setVisibility(View.GONE);
                                            episodeList.add(new Episode(Integer.parseInt(document.get("id").toString()),
                                                    Integer.parseInt(document.get("series_id").toString()),
                                                    Integer.parseInt(document.get("season").toString()),
                                                    document.getId(), document.getString("title"),
                                                    document.getString("photo"), document.getString("video"),
                                                    document.getString("duration"), document.getString("time"),
                                                    Integer.parseInt(document.get("views").toString())));
                                        }
                                        myCallback.onCallback(seriesList, episodeList);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface MyCallback {
        void onCallback(List<Series> list, List<Episode> list2);
    }
}