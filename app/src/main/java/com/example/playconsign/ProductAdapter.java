package com.example.playconsign;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        Log.e("SellerDebug", "check 1!" + productSellerUID);
        Log.e("SellerDebug", "check!");
        sellersDatabaseReference.child(productSellerUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Seller seller = snapshot.getValue(Seller.class);
                if (seller != null) {
                    holder.productSellerName.setText(seller.getShopName());
                    holder.productSellerCityAndCountry.setText(seller.getSellerCityAndCountry());
                } else {
                    holder.productSellerName.setText("Unknown Seller");
                    holder.productSellerCityAndCountry.setText("Unknown Location");
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
        TextView productSellerCityAndCountry;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.searchItemLayout_productName);
            productImage = itemView.findViewById(R.id.searchItemLayout_productImage);
            productPrice = itemView.findViewById(R.id.searchItemLayout_productPrice);
            productCategory = itemView.findViewById(R.id.searchItemLayout_productCategory);
            productCondition = itemView.findViewById(R.id.searchItemLayout_productCondition);
            productSellerName = itemView.findViewById(R.id.searchItemLayout_productSellerName);
            productSellerCityAndCountry = itemView.findViewById(R.id.searchItemLayout_productSellerCityAndCountry);
        }
    }
}
