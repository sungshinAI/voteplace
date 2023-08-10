package com.example.placetest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class VotePlaceActivity extends AppCompatActivity {
    private DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference("places");

    private RadioGroup placeRadioGroup;
    private Button voteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_place);

        placeRadioGroup = findViewById(R.id.placeRadioGroup);
        voteButton = findViewById(R.id.voteButton);

        loadPlaces();

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = placeRadioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedPlaceName = selectedRadioButton.getText().toString();

                    // 선택한 장소에 투표
                    placesRef.child(selectedPlaceName).runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Long votes = mutableData.getValue(Long.class);
                            if (votes == null) {
                                votes = 1L;
                            } else {
                                votes++;
                            }
                            mutableData.setValue(votes);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            if (databaseError != null) {
                                // 업데이트 중 오류 처리
                            } else {
                                Toast.makeText(VotePlaceActivity.this, "투표가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadPlaces() {
        placesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeRadioGroup.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String placeName = snapshot.getKey();

                    RadioButton radioButton = new RadioButton(VotePlaceActivity.this);
                    radioButton.setText(placeName);

                    placeRadioGroup.addView(radioButton);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 처리 중 오류 발생 시 처리
            }
        });
    }
}

