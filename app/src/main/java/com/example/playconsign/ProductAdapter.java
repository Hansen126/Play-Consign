package com.example.playconsign;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    Context context;
    DatabaseReference sellersDatabaseReference = FirebaseDatabase.getInstance().getReference("sellers");
    Seller productSeller;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layoutView = inflater.inflate(R.layout.search_item_layout, parent, false);
        ProductViewHolder holder = new ProductViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {

        String productName = productList.get(position).getProductName();
        String productImage = productList.get(position).getProductImage();
        int productPrice = productList.get(position).getProductPrice();
        String productCategory = productList.get(position).getProductCategory();
        String productCondition = productList.get(position).getProductCondition();
        String productDesc = productList.get(position).getProductDesc();
        String productSellerUID = productList.get(position).getProductSellerUID();

        int screenWidthDp = getScreenWidthInDp(holder.itemView.getContext());
        if(screenWidthDp < 427) {
            Pair<Integer, Integer> dimensions = calculateDynamicSize(screenWidthDp);

            int dynamicWidthPx = Math.round(dimensions.first * holder.itemView.getContext().getResources().getDisplayMetrics().density);
            int dynamicHeightPx = Math.round(dimensions.second * holder.itemView.getContext().getResources().getDisplayMetrics().density);

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.width = dynamicWidthPx;
            layoutParams.height = dynamicHeightPx;
            holder.itemView.setLayoutParams(layoutParams);
        }


        Log.e("SellerDebug", "check 1!" + productDesc);
        Log.e("SellerDebug", "check!");
        sellersDatabaseReference.child(productSellerUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Seller seller = snapshot.getValue(Seller.class);
                if (seller != null) {
                    holder.productSellerName.setText(seller.getShopName());
                    holder.productSellerDomicile.setText(seller.getSellerDomicile());
                } else {
                    holder.productSellerName.setText("Unknown Seller");
                    holder.productSellerDomicile.setText("Unknown Location");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SellerFetch", "Error fetching seller data: " + error.getMessage());
            }
        });

        String newProductPrice = NumberFormat.getNumberInstance(new Locale("id", "ID")).format(productPrice);

        holder.productName.setText(productName);
        holder.productPrice.setText("Rp " + newProductPrice);
        holder.productCategory.setText(productCategory);
        holder.productCondition.setText(productCondition);
        Log.d("ProductAdapter", "Image URL: " + productImage);
        Glide.with(context)
                .load(productImage)
                .placeholder(R.drawable.ic_image)
                .into(holder.productImage);

        holder.searchItemLayout_productCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productName", productName);
                intent.putExtra("productImage", productImage);
                intent.putExtra("productPrice", newProductPrice);
                intent.putExtra("productCategory", productCategory);
                intent.putExtra("productCondition", productCondition);
                intent.putExtra("productDesc", productDesc);
                intent.putExtra("productSellerUID", productSellerUID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder {
        TextView productName;
        ImageView productImage;
        TextView productPrice;
        TextView productCategory;
        TextView productCondition;
        TextView productSellerName;
        TextView productSellerDomicile;
        ConstraintLayout searchItemLayout_productCL;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.searchItemLayout_productName);
            productImage = itemView.findViewById(R.id.searchItemLayout_productImage);
            productPrice = itemView.findViewById(R.id.searchItemLayout_productPrice);
            productCategory = itemView.findViewById(R.id.searchItemLayout_productCategory);
            productCondition = itemView.findViewById(R.id.searchItemLayout_productCondition);
            productSellerName = itemView.findViewById(R.id.searchItemLayout_productSellerName);
            productSellerDomicile = itemView.findViewById(R.id.searchItemLayout_productSellerDomicile);
            searchItemLayout_productCL = itemView.findViewById(R.id.searchItemLayout_productCL);
        }
    }
    private Pair<Integer, Integer> calculateDynamicSize(int screenWidthDp) {
        int baseWidth = 180;
        int baseHeight = 384;
        int maxScreenWidth = 427;

        float scaleFactor = Math.max(0.5f, (float) screenWidthDp / maxScreenWidth);

        int dynamicWidth = Math.round(baseWidth * scaleFactor);
        int dynamicHeight = Math.round(baseHeight * scaleFactor);

        return new Pair<>(dynamicWidth, dynamicHeight);
    }


    private int getScreenWidthInDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(displayMetrics.widthPixels / displayMetrics.density);
    }
}
