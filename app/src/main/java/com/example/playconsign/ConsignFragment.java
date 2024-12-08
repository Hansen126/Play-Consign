package com.example.playconsign;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsignFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConsignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsignFragment newInstance(String param1, String param2) {
        ConsignFragment fragment = new ConsignFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    List<String> categoryList = new ArrayList<>();
    List<String> conditionList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        categoryList.add("Select Category");
        categoryList.add("Mouse");
        categoryList.add("Keyboard");
        categoryList.add("Headset");
        categoryList.add("Monitor");
        categoryList.add("PC");
        categoryList.add("Laptop");
        categoryList.add("Phone");
        categoryList.add("PC Parts");
        categoryList.add("Console");
        categoryList.add("Others");

        conditionList.add("Select Condition");
        conditionList.add("Brand New In Box");
        conditionList.add("Brand New Out Box");
        conditionList.add("Second");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consign, container, false);
        EditText consignNameET = view.findViewById(R.id.consignNameET);
        EditText consignPriceET = view.findViewById(R.id.consignPriceNumber);
        Spinner categorySpinner = view.findViewById(R.id.consignCategorySpinner);
        Spinner conditionSpinner = view.findViewById(R.id.consignConditionSpinner);
        EditText consignDescET = view.findViewById(R.id.consignDescMLT);
        ImageView consignImageIV = view.findViewById(R.id.consignImageIV);
        CheckBox consignTnCCB = view.findViewById(R.id.consignTnCCB);
        Button consignButton = view.findViewById(R.id.consignSubmitButton);
        String productSellerUID = currentUser.getUid();

//        sellersDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                productSeller = new Seller();
//                productSeller.setShopName(snapshot.child(sellerUID).child("shopName").getValue(String.class));
//                productSeller.setSellerDomicile(snapshot.child(sellerUID).child("sellerDomicile").getValue(String.class));
//                productSeller.setIDNumber(snapshot.child(sellerUID).child("IDNumber").getValue(String.class));
////                productSeller.setUser(snapshot.child(sellerUID).child("user").getValue(User.class));
//                if (productSeller != null) {
//                    Log.d("SellerDebug", "Seller loaded: " + productSeller.getShopName() + ", " + productSeller.getSellerDomicile());
//                } else {
//                    Log.e("SellerDebug", "Seller is null!");
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, conditionList);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);
        conditionSpinner.setAdapter(conditionAdapter);

        consignImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        if (imageUri != null) {
                            consignImageIV.setImageURI(imageUri);
                        }
                    }
                }
        );

        TextView TnCTextView = view.findViewById(R.id.consignTnCTV);
        TnCTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tncIntent = new Intent(getContext(), TnCActivity.class);
                getContext().startActivity(tncIntent);
            }
        });
        consignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(consignNameET.getText().toString().isEmpty()) {
                    consignNameET.setError("Please Enter Your Product Name");
                } else if(consignNameET.getText().toString().length() < 10 || consignNameET.getText().toString().length() > 50) {
                    consignNameET.setError("Product Name Must Be Between 10 and 50 Characters");
                } else if(consignPriceET.getText().toString().isEmpty()) {
                    consignPriceET.setError("Please Enter Your Product Price");
                } else if(Integer.parseInt(consignPriceET.getText().toString()) < 10000 || consignPriceET.getText().toString().length() > 10) {
                    consignPriceET.setError("Product Price Must Be Atleast 10.000");
                } else if(Integer.parseInt(consignPriceET.getText().toString()) % 1000 != 0) {
                    consignPriceET.setError("Product Price Must Be Multiple of 1.000");
                } else if(categorySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Your Product Category", Toast.LENGTH_SHORT).show();
                } else if(conditionSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Your Product Condition", Toast.LENGTH_SHORT).show();
                } else if(consignDescET.getText().toString().isEmpty()) {
                    consignDescET.setError("Please Enter Your Product Description");
                } else if(consignDescET.getText().toString().length() < 10 || consignDescET.getText().toString().length() > 1000) {
                    consignDescET.setError("Product Description Must Be Between 10 and 1000 Characters");
                } else if (imageUri == null) {
                    Toast.makeText(getContext(), "Please select your product image", Toast.LENGTH_SHORT).show();
                } else if (productSellerUID == null) {
                    Toast.makeText(getContext(), "Seller information is not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                } else if (!consignTnCCB.isChecked()) {
                    consignTnCCB.setError("Please Agree to the Terms and Conditions");
                } else {
                    try {
                        CloudinaryManager.uploadImage(getContext(), imageUri, new CloudinaryManager.Callback() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                addToFirebaseDatabase(consignNameET.getText().toString(),
                                        Integer.parseInt(consignPriceET.getText().toString()),
                                        categorySpinner.getSelectedItem().toString(),
                                        conditionSpinner.getSelectedItem().toString(),
                                        consignDescET.getText().toString(), imageUrl, productSellerUID);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e) {
                        Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
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
                            Toast.makeText(getContext(), "Product consigned successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Failed to consign product!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}