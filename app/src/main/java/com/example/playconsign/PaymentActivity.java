package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private WebView paymentWebView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference sellersDatabaseReference = firebaseDatabase.getReference("sellers");
    private DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("users");
    private Seller seller;
    private User user;
    private String UID;
    private MidtransService service;
    private String productName;
    private int productPrice;
    private String productCategory;
    private String productCondition;
    private String productDesc;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(firebaseAuth.getCurrentUser() != null) {
            UID = firebaseAuth.getCurrentUser().getUid();
        }

        productName = getIntent().getStringExtra("PRODUCT_NAME");
        productPrice = getIntent().getIntExtra("PRODUCT_PRICE", 0);
        productCategory = getIntent().getStringExtra("PRODUCT_CATEGORY");
        productCondition = getIntent().getStringExtra("PRODUCT_CONDITION");
        productDesc = getIntent().getStringExtra("PRODUCT_DESC");
        imageUrl = getIntent().getStringExtra("PRODUCT_IMAGE");
        ImageView paymentBackButton = findViewById(R.id.paymentBackButton);
        paymentBackButton.setOnClickListener(v -> finish());

        paymentWebView = findViewById(R.id.paymentWebView);
        paymentWebView.getSettings().setJavaScriptEnabled(true);
        paymentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("success")) {
                    Toast.makeText(PaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent);
                    addToFirebaseDatabase(productName, productPrice, productCategory, productCondition, productDesc, imageUrl, UID);
                } else if (url.contains("failed")) {
                    Toast.makeText(PaymentActivity.this, "Payment Failed. Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String snapUrl = getIntent().getStringExtra("SNAP_URL");
        Log.d("PaymentActivity", "SNAP_URL: " + snapUrl);

        if (snapUrl != null) {
            paymentWebView.loadUrl(snapUrl);
        } else {
            startPayment();
        }
    }

    public void startPayment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MidtransService.class);

        sellersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seller = snapshot.child(firebaseAuth.getUid()).getValue(Seller.class);

                usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.child(firebaseAuth.getUid()).getValue(User.class);
                        if (seller != null && user != null) {
                            sendSnapRequest();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Seller not available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PaymentActivity.this, "Error in Retrieving User data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "Error in Retrieving Seller data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendSnapRequest() {
        if(firebaseAuth.getCurrentUser() != null){
            if(getIntent().getStringExtra("SNAP_URL") == null) {
                SnapRequest request = new SnapRequest("ORDER-" + UID + "-" + System.currentTimeMillis(), 5000, seller.getShopName(), user.getEmail(), user.getPhone());
                service.createSnapToken(request).enqueue(new Callback<SnapResponse>() {
                    @Override
                    public void onResponse(Call<SnapResponse> call, Response<SnapResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String snapToken = response.body().getSnapToken();
                            String snapUrl = "https://app.sandbox.midtrans.com/snap/v2/vtweb/" + snapToken;

                            Log.d("PaymentActivity", "SNAP_URL: " + snapUrl);
                            paymentWebView.loadUrl(snapUrl);
                        }
                    }

                    @Override
                    public void onFailure(Call<SnapResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                String snapUrl = getIntent().getStringExtra("SNAP_URL");
                paymentWebView.loadUrl(snapUrl);
            }
        }
    }

    private void addToFirebaseDatabase(String productName, int productPrice, String productCategory,
                                       String productCondition, String productDescription, String imageUrl,
                                       String productSellerUID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        String productId = databaseReference.push().getKey();

        Product newProduct = new Product(productName, productPrice, productCategory,productCondition,
                productDescription, imageUrl, productSellerUID);

        if (productId != null) {
            databaseReference.child(productId).setValue(newProduct)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Product consigned successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to consign product!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}