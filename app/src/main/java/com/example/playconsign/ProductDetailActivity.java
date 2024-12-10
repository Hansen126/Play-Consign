package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProductDetailActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference sellersDatabaseReference = firebaseDatabase.getReference("sellers");
    DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView detailLogoutIV = findViewById(R.id.detailLogoutIV);
        ImageView detailProductImageIV = findViewById(R.id.detailProductImageIV);
        TextView detailProductPriceTV = findViewById(R.id.detailProductPriceTV);
        TextView detailProductNameTV = findViewById(R.id.detailProductNameTV);
        TextView detailProductCategoryTV = findViewById(R.id.detailProductCategoryTV);
        TextView detailProductConditionTV = findViewById(R.id.detailProductConditionTV);
        TextView detailProductDescriptionTV = findViewById(R.id.detailProductDescriptionTV);
        Button detailContactSellerButton = findViewById(R.id.detailContactSellerButton);
        TextView detailProductSellerShopNameTV = findViewById(R.id.detailProductSellerShopNameTV);
        TextView detailProductSellerDomicileTV = findViewById(R.id.detailProductSellerDomicileTV);
        TextView detailProductSellerPhoneTV = findViewById(R.id.detailProductSellerPhoneTV);

        detailLogoutIV.setOnClickListener(v -> finish());

        Intent getIntent = getIntent();
        String productPrice = getIntent.getStringExtra("productPrice");
        String productName = getIntent.getStringExtra("productName");
        String productImage = getIntent.getStringExtra("productImage");
        String productCategory = getIntent.getStringExtra("productCategory");
        String productCondition = getIntent.getStringExtra("productCondition");
        String productDesc = getIntent.getStringExtra("productDesc");
        String productSellerUID = getIntent.getStringExtra("productSellerUID");

        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.ic_image)
                .into(detailProductImageIV);

        detailProductNameTV.setText(productName);
        detailProductPriceTV.setText("Rp " + productPrice);
        detailProductCategoryTV.setText(productCategory);
        detailProductConditionTV.setText(productCondition);
        detailProductDescriptionTV.setText(productDesc);

        sellersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Seller seller = snapshot.child(productSellerUID).getValue(Seller.class);
                detailProductSellerShopNameTV.setText(seller.getShopName());
                detailProductSellerDomicileTV.setText(seller.getSellerDomicile());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detailProductSellerPhoneTV.setText(snapshot.child(productSellerUID).child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}