package com.rivierasoft.historicalturkishseries;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeriesSlideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeriesSlideFragment extends Fragment {

    ImageView imageView, showImageView, allIV;
    TextView textView, seasonsTextView, showTextView;
    Button button;
    LinearLayout linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";

    // TODO: Rename and change types of parameters
    private int id;
    private String name;
    private String picture;
    private String seasons;
    private String display_status;
    private boolean display_icon;

    public SeriesSlideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScreenSlidePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeriesSlideFragment newInstance(int param1, String param2, String param3, String param4, String param5, boolean param6) {
        SeriesSlideFragment fragment = new SeriesSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putBoolean(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            picture = getArguments().getString(ARG_PARAM3);
            seasons = getArguments().getString(ARG_PARAM4);
            display_status = getArguments().getString(ARG_PARAM5);
            display_icon = getArguments().getBoolean(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_slide, container, false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image);
        showImageView = view.findViewById(R.id.imageView3);
        allIV = view.findViewById(R.id.iv_all);
        textView = view.findViewById(R.id.textView);
        seasonsTextView = view.findViewById(R.id.textView2);
        showTextView = view.findViewById(R.id.textView3);
        button = view.findViewById(R.id.show_button);
        linearLayout = view.findViewById(R.id.linearLayout);

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        seasonsTextView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

        if (id != 0) {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(picture)
                    .into(imageView);

            textView.setText(name);
            seasonsTextView.setText(seasons);
            showTextView.setText(display_status);
            if (display_icon)
                showImageView.setImageDrawable(getResources().getDrawable(R.drawable.dot_green));
            else showImageView.setImageDrawable(getResources().getDrawable(R.drawable.dot));
        } else {
            button.setText("كل المسلسلات");
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.GONE);
            seasonsTextView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            allIV.setVisibility(View.VISIBLE);
        }



        button.setOnClickListener(v -> {
            if (id != 0)
                SeriesSlideFragment.this.startActivity(new Intent(SeriesSlideFragment.this.getActivity(), SeriesActivity.class).putExtra("id", id)
                        .putExtra("current_season", 1));
            else SeriesSlideFragment.this.startActivity(new Intent(SeriesSlideFragment.this.getActivity(), ContainerActivity.class)
                    .putExtra("fragment", 1));
        });
    }
}