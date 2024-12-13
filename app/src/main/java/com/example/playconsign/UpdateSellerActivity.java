package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class UpdateSellerActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sellersDatabaseReference = firebaseDatabase.getReference("sellers");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_seller);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText updateSellerShopName = findViewById(R.id.updateSellerShopNameET);
        EditText updateSellerCity = findViewById(R.id.updateSellerCityET);
        EditText updateSellerProvince = findViewById(R.id.updateSellerProvinceET);
        EditText updateSellerCountry = findViewById(R.id.updateSellerCountryET);
        TextView updateSellerCurrentDomicile = findViewById(R.id.updateSellerCurrectDomicileTV);
        ImageView updateSellerBackButton = findViewById(R.id.updateSellerBackButton);
        Button updateSellerSubmitButton = findViewById(R.id.updateSellerSubmitButton);

        if (firebaseAuth.getCurrentUser() != null) {
            sellersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String UID = firebaseAuth.getCurrentUser().getUid();
                    updateSellerShopName.setText(snapshot.child(UID).child("shopName").getValue().toString());
                    updateSellerCurrentDomicile.setText("Current Domicile: " +snapshot.child(UID).child("sellerDomicile").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        updateSellerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateSellerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null) {
                    if (updateSellerShopName.getText().toString().isEmpty()) {
                        Toast.makeText(UpdateSellerActivity.this, "Shop Name cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if(updateSellerCity.getText().toString().isEmpty()) {
                        Toast.makeText(UpdateSellerActivity.this, "City cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if(updateSellerProvince.getText().toString().isEmpty()) {
                        Toast.makeText(UpdateSellerActivity.this, "Province cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if(updateSellerCountry.getText().toString().isEmpty()) {
                        Toast.makeText(UpdateSellerActivity.this, "Country cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        String shopName = updateSellerShopName.getText().toString();
                        String city = updateSellerCity.getText().toString();
                        String province = updateSellerProvince.getText().toString();
                        String country = updateSellerCountry.getText().toString();
                        String domicile = city + ", " + province + ", " + country;
                        String UID = firebaseAuth.getCurrentUser().getUid();
                        sellersDatabaseReference.child(UID).child("shopName").setValue(shopName);
                        sellersDatabaseReference.child(UID).child("sellerDomicile").setValue(domicile);

                        Toast.makeText(UpdateSellerActivity.this, "Seller Information Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateSellerActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });


    }
}