package com.example.playconsign;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    List<Category> categoryList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Category mouse = new Category("Mouse", R.drawable.ic_mouse);
        Category keyboard = new Category("Keyboard", R.drawable.ic_keyboard);
        Category headset = new Category("Headset", R.drawable.ic_headset);
        Category monitor = new Category("Monitor", R.drawable.ic_monitor);
        Category pc = new Category("PC", R.drawable.ic_pc);
        Category laptop = new Category("Laptop", R.drawable.ic_laptop);
        Category phone = new Category("Phone", R.drawable.ic_phone);
        Category parts = new Category("PC Parts", R.drawable.ic_pc_parts);
        Category console = new Category("Console", R.drawable.ic_console);
        Category other = new Category("Others", R.drawable.ic_other);

        categoryList.add(mouse);
        categoryList.add(keyboard);
        categoryList.add(headset);
        categoryList.add(monitor);
        categoryList.add(pc);
        categoryList.add(laptop);
        categoryList.add(phone);
        categoryList.add(parts);
        categoryList.add(console);
        categoryList.add(other);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        TextView homeNameTV = view.findViewById(R.id.homeNameTV);
        if(firebaseAuth.getCurrentUser() != null){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("users");

            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        homeNameTV.setText("Hello, " + name);
                    } else {
                        Log.d("Firebase", "User does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to read user data", error.toException());
                }
            });

        } else {
            homeNameTV.setText("Hello, Guest");
        }

        ImageView homeExploreIV = view.findViewById(R.id.homeExploreIV);
        homeExploreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        RecyclerView categoryRV = view.findViewById(R.id.homeCategoryRV);
        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        categoryRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        categoryRV.setAdapter(adapter);

        SearchView homeSearchView = view.findViewById(R.id.homeSearchView);
        homeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(view.getContext(), SearchActivity.class);
                searchIntent.putExtra("query", homeSearchView.getQuery());
                view.getContext().startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
}