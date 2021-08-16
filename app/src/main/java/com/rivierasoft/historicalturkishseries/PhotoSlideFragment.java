package com.rivierasoft.historicalturkishseries;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoSlideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoSlideFragment extends Fragment {

    PhotoView imageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotoSlideFragment() {
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
    public static PhotoSlideFragment newInstance(String param1, String param2) {
        PhotoSlideFragment fragment = new PhotoSlideFragment();
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
        return inflater.inflate(R.layout.fragment_photo_slide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image);

        Picasso.with(getActivity())
                .load(mParam1)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_broken)
                .into(imageView);

//        Glide.with(getActivity())
//                .asBitmap()
//                .load(mParam1)
//                .into(imageView);

        //textView.setText(mParam2);

        /*downloadImageView.setOnClickListener(v -> {
            DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(mParam1);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("المؤسس عثمان");
            request.setDescription("التنزيل");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            //request.setDestinationUri(Uri.parse("file://" + "Images" + "/ertugrul.jpg"));
            //request.setDestinationInExternalFilesDir(context, DIRECTORY_DOWNLOADS, "ertugrul.jpg");
            downloadmanager.enqueue(request);
        });*/
    }
}