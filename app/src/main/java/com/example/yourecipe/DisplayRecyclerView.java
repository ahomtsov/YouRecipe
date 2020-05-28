package com.example.yourecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.yourecipe.adapter.RecycleViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayRecyclerView extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private RecyclerView.Adapter recycleViewAdapter;
    private RecycleViewAdapter recycleViewAdapter;
    private DatabaseReference dbRef;
    private HashMap<String, Long> productsMap;
    private List<String> products;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recycler_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Выбор продуктов");

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Long>> t = new GenericTypeIndicator<HashMap<String, Long>>() {};
                productsMap = dataSnapshot.child("product").getValue(t);
                products = new ArrayList<String>(productsMap.keySet());
                //products.remove(0);
                System.out.println("Products: " + products);
                loadProducts(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recyclerViewChooseProduct);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recycleViewAdapter = new RecycleViewAdapter();
        recyclerView.setAdapter(recycleViewAdapter);

        //loadProducts();
        //recycleViewAdapter.setItems(products);
    }

    private void loadProducts(List products) {
        //Collection products = getProducts();
        recycleViewAdapter.setItems(products);
    }
/*
    private Collection<String> getProducts() {
        return Arrays.asList(
                new String("qwerty"),

                new String("asdfg"),

                new String("zxcvb")
        );
    }*/

}
