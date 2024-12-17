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
import android.widget.FrameLayout;
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
    private FrameLayout progressBar;

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
        progressBar = view.findViewById(R.id.progressOverlay);
        String productSellerUID = currentUser.getUid();

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
                consignButton.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(consignNameET.getText().toString().isEmpty()) {
                    consignNameET.setError("Please Enter Your Product Name");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(consignNameET.getText().toString().length() < 10 || consignNameET.getText().toString().length() > 50) {
                    consignNameET.setError("Product Name Must Be Between 10 and 50 Characters");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(consignPriceET.getText().toString().isEmpty()) {
                    consignPriceET.setError("Please Enter Your Product Price");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(Integer.parseInt(consignPriceET.getText().toString()) < 10000 || consignPriceET.getText().toString().length() > 10) {
                    consignPriceET.setError("Product Price Must Be Atleast 10.000");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(Integer.parseInt(consignPriceET.getText().toString()) % 1000 != 0) {
                    consignPriceET.setError("Product Price Must Be Multiple of 1.000");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(categorySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Your Product Category", Toast.LENGTH_SHORT).show();
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(conditionSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Please Select Your Product Condition", Toast.LENGTH_SHORT).show();
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(consignDescET.getText().toString().isEmpty()) {
                    consignDescET.setError("Please Enter Your Product Description");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if(consignDescET.getText().toString().length() < 10 || consignDescET.getText().toString().length() > 5000) {
                    consignDescET.setError("Product Description Must Be Atleast 10 and 5000 Characters");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if (imageUri == null) {
                    Toast.makeText(getContext(), "Please select your product image", Toast.LENGTH_SHORT).show();
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if (productSellerUID == null) {
                    Toast.makeText(getContext(), "Seller information is not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else if (!consignTnCCB.isChecked()) {
                    consignTnCCB.setError("Please Agree to the Terms and Conditions");
                    consignButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else {
                    try {
                        CloudinaryManager.uploadImage(getContext(), imageUri, new CloudinaryManager.Callback() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getContext().startActivity(intent);
                                startPayment(consignNameET.getText().toString(),
                                        Integer.parseInt(consignPriceET.getText().toString()),
                                        categorySpinner.getSelectedItem().toString(),
                                        conditionSpinner.getSelectedItem().toString(),
                                        consignDescET.getText().toString(),
                                        imageUrl);



//                                addToFirebaseDatabase(consignNameET.getText().toString(),
//                                        Integer.parseInt(consignPriceET.getText().toString()),
//                                        categorySpinner.getSelectedItem().toString(),
//                                        conditionSpinner.getSelectedItem().toString(),
//                                        consignDescET.getText().toString(), imageUrl, productSellerUID);

                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                consignButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    catch (Exception e) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                        Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        consignButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
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

    private void startPayment(String name, int price, String category, String condition, String description, String imageURL) {
        Intent paymentIntent = new Intent(getContext(), PaymentActivity.class);
        paymentIntent.putExtra("PRODUCT_NAME", name);
        paymentIntent.putExtra("PRODUCT_PRICE", price);
        paymentIntent.putExtra("PRODUCT_CATEGORY", category);
        paymentIntent.putExtra("PRODUCT_CONDITION", condition);
        paymentIntent.putExtra("PRODUCT_DESC", description);
        paymentIntent.putExtra("PRODUCT_IMAGE", imageURL); // Uri to String
        startActivity(paymentIntent);
    }

//    private void addToFirebaseDatabase(String productName, int productPrice, String productCategory,
//                                       String productCondition, String productDescription, String imageUrl,
//                                       String productSellerUID) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
//        String productId = databaseReference.push().getKey();
//
//        Product newProduct = new Product(productName, productPrice, productCategory,productCondition,
//                productDescription, imageUrl, productSellerUID);
//
//        if (productId != null) {
//            databaseReference.child(productId).setValue(newProduct)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), "Product consigned successfully!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getContext(), "Failed to consign product!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
}