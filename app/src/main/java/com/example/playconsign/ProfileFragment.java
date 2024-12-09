package com.example.playconsign;

import static android.view.View.GONE;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("users");
    DatabaseReference sellersDatabaseReference = firebaseDatabase.getReference("sellers");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  declare all component
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView profileLogoutIV = view.findViewById(R.id.profileLogoutIV);
        ImageView profilePictureIV = view.findViewById(R.id.profilePictureIV);
        TextView profileNameTV = view.findViewById(R.id.profileNameTV);
        TextView profileEmailTV = view.findViewById(R.id.profileEmailTV);
        TextView profilePhoneTV = view.findViewById(R.id.profilePhoneTV);
        TextView profileSellerShopNameTV = view.findViewById(R.id.profileSellerShopNameTV);
        TextView profileSellerDomicileTV = view.findViewById(R.id.profileSellerDomicileTV);
        TextView profileSellerUpdateTV = view.findViewById(R.id.profileSellerUpdateTV);
        Button profileTransactionButton = view.findViewById(R.id.profileTransactionButton);
        LinearLayout profileSellerLL = view.findViewById(R.id.profileSellerLL);



        if(currentUser != null) {
            String UID = currentUser.getUid();

            profileLogoutIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    Toast.makeText(getActivity(), "Successfuly Logout", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(homeIntent);

                }
            });

            usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.child(UID).getValue(User.class);
                    profileNameTV.setText(user.getName());
                    profileEmailTV.setText(user.getEmail());
                    profilePhoneTV.setText(user.getPhone());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            profileSellerLL.setVisibility(view.GONE);

            sellersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(UID).exists()) {
                        profileSellerLL.setVisibility(View.VISIBLE);
                        Seller seller = snapshot.child(UID).getValue(Seller.class);
                        profileSellerShopNameTV.setText(seller.getShopName());
                        profileSellerDomicileTV.setText(seller.getSellerDomicile());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            profileSellerUpdateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sellerIntent = new Intent(getActivity(), UpdateSellerActivity.class);
                    startActivity(sellerIntent);
                }
            });

            profileTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transactionIntent = new Intent(getActivity(), TransactionActivity.class);
                    startActivity(transactionIntent);
                }
            });
        } else {
            Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }





        return view;
    }
}