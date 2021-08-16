package com.rivierasoft.historicalturkishseries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDataActivity extends AppCompatActivity {

    private EditText idET, seriesIdET, seasonET;
    private Button addBT;

    private int id, series_id, season;
    private Map<String, Object> episode;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference episodesReference = db.collection("Episodes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        idET = findViewById(R.id.et_id);
        seriesIdET = findViewById(R.id.et_series_id);
        seasonET = findViewById(R.id.et_season);
        addBT = findViewById(R.id.bt_add);

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Integer.parseInt(idET.getText().toString());
                series_id = Integer.parseInt(seriesIdET.getText().toString());
                season = Integer.parseInt(seasonET.getText().toString());
                episode = new HashMap<>();
                episode.put("id", id);
                episode.put("series_id", series_id);
                episode.put("season", season);
                episode.put("title", "");
                episode.put("photo", "");
                episode.put("video", "");
                episode.put("views", "");
                episode.put("duration", "");
                episode.put("time", "");
                episode.put("date", "");

                episodesReference.document(series_id + getID(id)).set(episode).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successful adding document", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding document", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public String getID(int id) {
        if (id <10)
            return id+"";
        else if (id <20)
            return "a"+(id-10)+"";
        else if (id <30)
            return "b"+(id-20)+"";
        else if (id <40)
            return "c"+(id-30)+"";
        else if (id <50)
            return "d"+(id-40)+"";
        else if (id <60)
            return "e"+(id-50)+"";
        else if (id <70)
            return "f"+(id-60)+"";
        else if (id <80)
            return "g"+(id-70)+"";
        else if (id <90)
            return "h"+(id-80)+"";
        else if (id <100)
            return "i"+(id-90)+"";
        else if (id <110)
            return "j"+(id-100)+"";
        else if (id <120)
            return "k"+(id-110)+"";
        else if (id <130)
            return "l"+(id-120)+"";
        else if (id <140)
            return "m"+(id-130)+"";
        else if (id <150)
            return "n"+(id-140)+"";
        else if (id <160)
            return "o"+(id-150)+"";
        else if (id <170)
            return "p"+(id-160)+"";
        else if (id <180)
            return "q"+(id-170)+"";
        else if (id <190)
            return "r"+(id-180)+"";
        else if (id <200)
            return "s"+(id-190)+"";
        else if (id <210)
            return "t"+(id-200)+"";
        else return null;
    }
}