package com.example.playconsign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Button detailContactSellerButton2 = findViewById(R.id.detailContactSellerButton2);
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
                String sellerPhone = snapshot.child(productSellerUID).child("phone").getValue(String.class);
                detailProductSellerPhoneTV.setText(sellerPhone);
                detailContactSellerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sellerPhoneNumber = "+62" + sellerPhone;
                        String message = "Hello, I'm interested in your product. Can i ask about " + productName + "?";
                        try {
                            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                            whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + sellerPhoneNumber + "&text=" + Uri.encode(message)));
                            startActivity(whatsappIntent);
                        } catch (Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                detailContactSellerButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sellerPhoneNumber = "+62" + sellerPhone;
                        String message = "Hello, I'm interested in your product. Can i ask about " + productName + "?";
                        try {
                            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                            whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + sellerPhoneNumber + "&text=" + Uri.encode(message)));
                            startActivity(whatsappIntent);
                        } catch (Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}