package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText updateProfileNameET = findViewById(R.id.updateProfileNameET);
        EditText updateProfileAddressET = findViewById(R.id.updateProfileAddressET);
        ImageView updateProfileBackButton = findViewById(R.id.updateProfileBackButton);
        Button updateProfileSubmitButton = findViewById(R.id.updateProfileSubmitButton);

        if (firebaseAuth.getCurrentUser() != null) {
            String UID = firebaseAuth.getCurrentUser().getUid();
            usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    updateProfileNameET.setText(snapshot.child(UID).child("name").getValue().toString());
                    updateProfileAddressET.setText(snapshot.child(UID).child("address").getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        updateProfileBackButton.setOnClickListener(v -> finish());

        updateProfileSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String UID = firebaseAuth.getCurrentUser().getUid();
                    String name = updateProfileNameET.getText().toString();
                    String address = updateProfileAddressET.getText().toString();

                    usersDatabaseReference.child(UID).child("name").setValue(name);
                    usersDatabaseReference.child(UID).child("address").setValue(address);

                    Toast.makeText(UpdateProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });




    }
}