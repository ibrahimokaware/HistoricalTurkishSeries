package com.rivierasoft.historicalturkishseries;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Adler32;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeasonsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonsFragment extends Fragment {

    private ImageView cancelImageView;
    private TextView season1, season2, season3, season4, season5, season6, season7;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    // TODO: Rename and change types of parameters
    private int id;
    private int no_of_seasons;
    private int current_season;

    public SeasonsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeasonsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeasonsFragment newInstance(int param1, int param2, int param3) {
        SeasonsFragment fragment = new SeasonsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            no_of_seasons = getArguments().getInt(ARG_PARAM2);
            current_season = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seasons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelImageView = view.findViewById(R.id.cancelImageView);
        season1 = view.findViewById(R.id.season1);
        season2 = view.findViewById(R.id.season2);
        season3 = view.findViewById(R.id.season3);
        season4 = view.findViewById(R.id.season4);
        season5 = view.findViewById(R.id.season5);
        season6 = view.findViewById(R.id.season6);
        season7 = view.findViewById(R.id.season7);

        switch (no_of_seasons) {
            case 3:
                season3.setVisibility(View.VISIBLE);
                break;
            case 4:
                season3.setVisibility(View.VISIBLE);
                season4.setVisibility(View.VISIBLE);
                break;
            case 5:
                season3.setVisibility(View.VISIBLE);
                season4.setVisibility(View.VISIBLE);
                season5.setVisibility(View.VISIBLE);
                break;
            case 6:
                season3.setVisibility(View.VISIBLE);
                season4.setVisibility(View.VISIBLE);
                season5.setVisibility(View.VISIBLE);
                season6.setVisibility(View.VISIBLE);
                break;
            case 7:
                season3.setVisibility(View.VISIBLE);
                season4.setVisibility(View.VISIBLE);
                season5.setVisibility(View.VISIBLE);
                season6.setVisibility(View.VISIBLE);
                season7.setVisibility(View.VISIBLE);
                break;
        }

        switch (current_season) {
            case 1:
                season1.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                season2.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 3:
                season3.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 4:
                season4.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 5:
                season5.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 6:
                season6.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case 7:
                season7.setTextColor(getResources().getColor(android.R.color.white));
                break;
        }

        season1.setOnClickListener(v -> {
            current_season = 1;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season2.setOnClickListener(v -> {
            current_season = 2;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season3.setOnClickListener(v -> {
            current_season = 3;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season4.setOnClickListener(v -> {
            current_season = 4;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season5.setOnClickListener(v -> {
            current_season = 5;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season6.setOnClickListener(v -> {
            current_season = 6;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        season7.setOnClickListener(v -> {
            current_season = 7;
            getActivity().finish();
            startActivity(new Intent(getActivity(), SeriesActivity.class).putExtra("id", id).putExtra("current_season", current_season));
        });

        cancelImageView.setOnClickListener(t -> getActivity().onBackPressed());
    }
}