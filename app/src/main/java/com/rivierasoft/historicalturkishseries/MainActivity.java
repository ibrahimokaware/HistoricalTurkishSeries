package com.rivierasoft.historicalturkishseries;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    ProgressDialog progressDialog;
    DateFormat dateFormat;

    private static final int NUM_PAGES = 4;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;

    private String share, rate, ourApps;
    private boolean isEarn, isEarn2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference seriesReference = db.collection("FrontSeries");
    private CollectionReference episodesReference = db.collection("Episodes");

    private ArrayList<Episode> episodeArrayList;
    private List<Episode> episodeList = new ArrayList<>();
    private ArrayList<Episode> episodeArrayList2;
    private List<Episode> episodeList2 = new ArrayList<>();
    private List<Series> seriesList = new ArrayList<>();

    private ArrayList<Episode> arrayList;


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
        setContentView(R.layout.activity_main);

        AdsCenter.loadInterstitialAd(getApplicationContext());
        AdsCenter.loadRewardedAd(getApplicationContext());

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorBar));
//        }

        sharedPreferences = getSharedPreferences("views" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tabLayout = findViewById(R.id.into_tab_layout);

        viewPager = findViewById(R.id.pager);

        constraintLayout = findViewById(R.id.constraintLayout);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);

        readData((list, list2, list3) -> {
            episodeArrayList = (ArrayList<Episode>) list2;
            episodeArrayList2 = (ArrayList<Episode>) list3;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            viewPager.setPageTransformer(new ZoomOutPageTransformer());
            pagerAdapter = new ScreenSlidePagerAdapter(this);
            viewPager.setAdapter(pagerAdapter);

            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.view.setClickable(false)
            ).attach();

            recyclerView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);


            navigationView.bringToFront();
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
            //{
//                @Override
//                public void onDrawerStateChanged(int newState) {
//                    if (newState == DrawerLayout.STATE_DRAGGING  && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        Toast.makeText(getApplicationContext(), "STAT OPENING", Toast.LENGTH_SHORT).show();
//                    }
//                    super.onDrawerStateChanged(newState);
//                }
            //};
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);

            ArrayList<MainRecyclerView> mainRecyclerViews = new ArrayList<>();
            mainRecyclerViews.add(new MainRecyclerView("أحدث الحلقات", episodeArrayList, new EpisodeAdapter(episodeArrayList, R.layout.episode_main, getApplicationContext(),
                    p -> {
                //startActivity(new Intent(getApplicationContext(), AddDataActivity.class));
                        if (AdsCenter.rewardedAd.isLoaded()) {
                            //r = r+1;
                            Activity activityContext = MainActivity.this;
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
                                        String document = episodeArrayList.get(p).getDocument();
                                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                                .putExtra("type", 1));
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
                            //Toast.makeText(getApplicationContext(), "لا يوجد اتصال بالإنترنت!", Toast.LENGTH_SHORT).show();
                            String document = episodeArrayList.get(p).getDocument();
                            startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                    .putExtra("type", 1));
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
            })));

            mainRecyclerViews.add(new MainRecyclerView("الأكثر مشاهدة", episodeArrayList2, new EpisodeAdapter(episodeArrayList2, R.layout.episode_main, getApplicationContext(),
                    p -> {
                        if (AdsCenter.rewardedAd.isLoaded()) {
                            //r = r+1;
                            Activity activityContext = MainActivity.this;
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
                                    if (!isEarn2)
                                        Toast.makeText(getApplicationContext(), "قم بمشاهدة الإعلان كاملاً لبدء الحلقة.", Toast.LENGTH_SHORT).show();
                                    else {
                                        String document = episodeArrayList2.get(p).getDocument();
                                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList2.get(p).getVideo())
                                                .putExtra("type", 1));
                                        if (sharedPreferences.getInt(document, 0) == 0) {
                                            editor.putInt(document, 1);
                                            editor.apply();
                                            episodesReference
                                                    .document(episodeArrayList2.get(p).getDocument())
                                                    .update("views", episodeArrayList2.get(p).getViews()+1)
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
                                    isEarn2 = true;
                                }

                                @Override
                                public void onRewardedAdFailedToShow(AdError adError) {
                                    // Ad failed to display.
                                    Toast.makeText(getApplicationContext(), "Ad failed to display.", Toast.LENGTH_SHORT).show();
                                }
                            };
                            AdsCenter.rewardedAd.show(activityContext, adCallback);
                        } else {
                            String document = episodeArrayList2.get(p).getDocument();
                            startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("video", episodeArrayList2.get(p).getVideo())
                                    .putExtra("type", 1));
                            if (sharedPreferences.getInt(document, 0) == 0) {
                                editor.putInt(document, 1);
                                editor.apply();
                                episodesReference
                                        .document(episodeArrayList2.get(p).getDocument())
                                        .update("views", episodeArrayList2.get(p).getViews()+1)
                                        .addOnSuccessListener(aVoid -> {
                                            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
            })));

            MainRecyclerViewAdapter mainRecyclerViewAdapter = new MainRecyclerViewAdapter(mainRecyclerViews, getApplicationContext(), new OnItemClickListener() {
                @Override
                public void OnClick(int p) {
                    if (p == 0)
                        startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 2));
                    else startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 3));
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mainRecyclerViewAdapter);
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
            finishAffinity();
        }

        /*if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item:
                    startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 1));
                break;
            case R.id.nav_item2:
                    startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 2));
                break;
            case R.id.nav_item3:
                startActivity(new Intent(getApplicationContext(), ContainerActivity.class).putExtra("fragment", 3));
                break;
            case R.id.nav_item4:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, share);
                sendIntent.setType("text/plain");
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
                break;
            case R.id.nav_item5:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(rate));
                    startActivity(intent);
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rate)));
                }
                break;
            case R.id.nav_item6:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(ourApps));
                    startActivity(intent);
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ourApps)));
                }
                break;
            case R.id.nav_item7:
                String url = "https://api.whatsapp.com/send?phone=+972599195534";
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.whatsapp");
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rivierasoft@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "المسلسلات التركية التاريخية/ ");

                    intent.setPackage("com.google.android.gm");
                    intent.setType("message/rfc822");
                    startActivity(intent);
                }
                break;
        }
        return true;
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
            Series series = seriesList.get(position);
                return SeriesSlideFragment.newInstance(series.getId(), series.getName(), series.getPicture(), series.getSeasons(),
                        series.getDisplay_status(), series.isDisplay());
            }

        @Override
        public int getItemCount() {
            return seriesList.size();
        }
    }

    public void readData(final MyCallback myCallback) {
            seriesReference
                    .orderBy("order")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals("0")) {
                                    share = document.getString("share");
                                    rate = document.getString("rate");
                                    ourApps = document.getString("our_apps");
                                }
                                seriesList.add(new Series(Integer.parseInt(document.get("id").toString()), document.getString("name"),
                                        document.getString("photo"), null, null,
                                        document.getString("seasons"), 0,
                                        null, document.getString("display_status"),
                                        (Boolean) document.get("display")));
                            }
                            episodesReference
                                    .orderBy("date", Query.Direction.DESCENDING)
                                    .limit(10)
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
                                            episodesReference
                                                    .orderBy("views", Query.Direction.DESCENDING)
                                                    .limit(10)
                                                    .get()
                                                    .addOnCompleteListener(task3 -> {
                                                        if (task3.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task3.getResult()) {
                                                                progressBar.setVisibility(View.GONE);
                                                                episodeList2.add(new Episode(Integer.parseInt(document.get("id").toString()),
                                                                        Integer.parseInt(document.get("series_id").toString()),
                                                                        Integer.parseInt(document.get("season").toString()),
                                                                        document.getId(), document.getString("title"),
                                                                        document.getString("photo"), document.getString("video"),
                                                                        document.getString("duration"), document.getString("time"),
                                                                        Integer.parseInt(document.get("views").toString())));
                                                            }
                                                            myCallback.onCallback(seriesList, episodeList, episodeList2);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
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
        void onCallback(List<Series> list, List<Episode> list2, List<Episode> list3);
    }
}