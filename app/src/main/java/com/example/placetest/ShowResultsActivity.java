package com.example.placetest;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placeList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String placeName = snapshot.getKey();   // 장소 이름 가져옴
                    Long votes = snapshot.child("votes").getValue(Long.class);  // 득표 수 가져옴
                    Place place = new Place(placeName, votes);  // 장소 객체 생성
                    placeList.add(place);  // 리스트에 추가
                }

                showResults();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 처리 중 오류 발생 시 처리
            }
        });
    }

    private void showResults() {
        LinearLayout resultsLayout = findViewById(R.id.resultsLayout);
        resultsLayout.removeAllViews();

        for (Place place : placeList) {
            View placeView = getLayoutInflater().inflate(R.layout.place_item, resultsLayout, false);
            TextView placeNameTextView = placeView.findViewById(R.id.placeNameTextView);
            TextView voteCountTextView = placeView.findViewById(R.id.voteCountTextView);

            placeNameTextView.setText(place.getName());

            if (place.getVotes() != null) {
                voteCountTextView.setText("투표 수: " + place.getVotes());
            }

            resultsLayout.addView(placeView);
        }
    }
}
