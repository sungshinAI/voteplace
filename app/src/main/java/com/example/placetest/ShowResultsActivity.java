package com.example.placetest;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowResultsActivity extends AppCompatActivity {
    private DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference("places");

    private List<Place> placeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        loadPlaces();
    }

    private void loadPlaces() {
        placesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String placeName = snapshot.getKey();
                    Place place = new Place(placeName);
                    placeList.add(place);
                }

                showResults();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 처리 중 오류 발생 시 처리
            }
        });
    }

    private void showResults() {
        LinearLayout resultsLayout = findViewById(R.id.resultsLayout);
        resultsLayout.removeAllViews();

        for (Place place : placeList) {
            View placeView = getLayoutInflater().inflate(R.layout.activity_show_results, resultsLayout, false);
            TextView placeNameTextView = placeView.findViewById(R.id.placeNameTextView);
            TextView voteCountTextView = placeView.findViewById(R.id.voteCountTextView);

            placeNameTextView.setText(place.getName());

            placesRef.child(place.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long votes = dataSnapshot.getValue(Long.class);
                    if (votes != null) {
                        voteCountTextView.setText("투표 수: " + votes);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 처리 중 오류 발생 시 처리
                }
            });

            resultsLayout.addView(placeView);
        }
    }
}
