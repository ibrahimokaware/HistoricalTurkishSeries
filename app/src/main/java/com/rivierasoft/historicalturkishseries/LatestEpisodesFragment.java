package com.rivierasoft.historicalturkishseries;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LatestEpisodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LatestEpisodesFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private boolean isEarn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference episodesReference = db.collection("Episodes");

    private ArrayList<Episode> episodeArrayList;
    private List<Episode> episodeList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LatestEpisodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LatestEpisodesFragment newInstance(String param1, String param2) {
        LatestEpisodesFragment fragment = new LatestEpisodesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_episodes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("views" , MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar = getActivity().findViewById(R.id.toolbar);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

        readData((list) -> {
            episodeArrayList = (ArrayList<Episode>) list;

            recyclerView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);

            EpisodeAdapter episodeAdapter = new EpisodeAdapter(episodeArrayList, R.layout.episode, getActivity(),
                    p -> {
                        if (AdsCenter.rewardedAd.isLoaded()) {
                            //r = r+1;
                            Activity activityContext = getActivity();
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
                                        Toast.makeText(getActivity(), "قم بمشاهدة الإعلان كاملاً لبدء الحلقة.", Toast.LENGTH_SHORT).show();
                                    else {
                                        getActivity().onBackPressed();
                                        String document = episodeArrayList.get(p).getDocument();
                                        startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                                .putExtra("type", 3));
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
                                    Toast.makeText(getActivity(), "Ad failed to display.", Toast.LENGTH_SHORT).show();
                                }
                            };
                            AdsCenter.rewardedAd.show(activityContext, adCallback);
                        } else {
                            getActivity().onBackPressed();
                            String document = episodeArrayList.get(p).getDocument();
                            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("video", episodeArrayList.get(p).getVideo())
                                    .putExtra("type", 3));
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(episodeAdapter);
        });
    }

    public void readData(final MyCallback myCallback) {

        episodesReference
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            progressBar.setVisibility(View.GONE);
                            int id = Integer.parseInt(document.get("id").toString());
                            int series_id = Integer.parseInt(document.get("series_id").toString());
                            int season = Integer.parseInt(document.get("season").toString());
                            String documentString = document.getId();
                            String title = document.getString("title");
                            String photo = document.getString("photo");
                            String video = document.getString("video");
                            String duration = document.getString("duration");
                            String time = document.getString("time");
                            int views = Integer.parseInt(document.get("views").toString());
                            episodeList.add(new Episode(id, series_id, season, documentString, title, photo, video, duration, time, views));
                        }
                        myCallback.onCallback(episodeList);
                    } else {
                        Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface MyCallback {
        void onCallback(List<Episode> list);
    }
}