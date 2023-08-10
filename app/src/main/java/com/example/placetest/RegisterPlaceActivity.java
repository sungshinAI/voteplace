package com.example.placetest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPlaceActivity extends AppCompatActivity {
    private DatabaseReference placesRef = FirebaseDatabase.getInstance().getReference("places");

    private EditText placeNameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_place);

        placeNameEditText = findViewById(R.id.placeNameEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeName = placeNameEditText.getText().toString();
                if (!TextUtils.isEmpty(placeName)) {
                    // 새로운 장소 등록
                    String newPlaceKey = placesRef.push().getKey();
                    placesRef.child(newPlaceKey).setValue(placeName);

                    Toast.makeText(RegisterPlaceActivity.this, "장소가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterPlaceActivity.this, "장소 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
