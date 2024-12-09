package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerRegisterActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("sellers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText sellerRegisterShopNameET = findViewById(R.id.sellerRegisterShopNameET);
        EditText sellerRegisterIDNumberET = findViewById(R.id.sellerRegisterIDNumberET);
        EditText sellerRegisterCityET = findViewById(R.id.sellerRegisterCityET);
        EditText sellerRegisterProvinceET = findViewById(R.id.sellerRegisterProvinceET);
        EditText sellerRegisterCountryET = findViewById(R.id.sellerRegisterCountryET);
        CheckBox sellerRegisterTnCCB = findViewById(R.id.sellerRegisterTnCCB);
        TextView sellerRegisterTnCTV = findViewById(R.id.sellerRegisterTnCTV);
        ImageView sellerRegisterBackButton = findViewById(R.id.sellerRegisterBackButton);
        Button sellerRegisterSubmitButton = findViewById(R.id.sellerRegisterSubmitButton);

        sellerRegisterBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sellerRegisterTnCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(SellerRegisterActivity.this, TnCActivity.class);
                startActivity(newIntent);
            }
        });



        sellerRegisterSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shopName = sellerRegisterShopNameET.getText().toString();
                String IDNumber = sellerRegisterIDNumberET.getText().toString();
                String city = sellerRegisterCityET.getText().toString();
                String province = sellerRegisterProvinceET.getText().toString();
                String country = sellerRegisterCountryET.getText().toString();

                if(shopName.length() < 10 || shopName.length() > 30) {
                    sellerRegisterShopNameET.setError("Shop name must be between 10 and 30 characters");
                } else if(IDNumber.isEmpty()) {
                    sellerRegisterIDNumberET.setError("Please enter your ID number");
                } else if(city.isEmpty()) {
                    sellerRegisterCityET.setError("Please enter your city");
                } else if(province.isEmpty()) {
                    sellerRegisterProvinceET.setError("Please enter your province");
                } else if(country.isEmpty()) {
                    sellerRegisterCountryET.setError("Please enter your country");
                } else if(!sellerRegisterTnCCB.isChecked()) {
                    sellerRegisterTnCCB.setError("Please Agree to the terms and conditions");
                } else {
                    String domicile = city + ", " + province + ", " + country;
                    addToDatabase(shopName, IDNumber, domicile);
//                    Toast.makeText(this, "Seller registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void addToDatabase(String shopName, String IDNumber, String domicile) {
        if(firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Seller newSeller = new Seller(IDNumber, domicile, shopName);
            databaseReference.child(userId).setValue(newSeller);
        } else {
            Log.d("register", "addUserToDatabase: currentuser is null");
        }
    }
}