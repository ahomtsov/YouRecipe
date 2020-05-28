package com.example.yourecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yourecipe.adapter.RecipeViewAdapter;
import com.example.yourecipe.adapter.RecycleViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayRecipeCategory extends AppCompatActivity {

    private DatabaseReference dbRef;
    private StorageReference mStorageRef;
    private HashMap<String, Long> recipeMap;
    private List<String> recipes;
    private List<String> imgPath;
    private RecyclerView recyclerView;
    private RecipeViewAdapter recipeViewAdapter;
    String path;
    String title;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_category);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int keyOfButton = getIntent().getIntExtra("keyOfButton", 0);

        if (keyOfButton == 1) {
            path = "recipeFirstCourse";
            title = "Первые блюда";
        }
        if (keyOfButton == 2) {
            path = "recipeSecondCourse";
            title = "Вторые блюда";
        }
        if (keyOfButton == 3) {
            path = "recipeSalad";
            title = "Салаты";
        }
        if (keyOfButton == 4) {
            path = "recipeDessert";
            title = "Дессерты";
        }

        setTitle(title);

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Long>> t = new GenericTypeIndicator<HashMap<String, Long>>() {};
                recipeMap = dataSnapshot.child(path).getValue(t);
                recipes = new ArrayList<String>(recipeMap.keySet());
                imgPath = new ArrayList<String>(recipeMap.keySet());
                //products.remove(0);
                System.out.println("Recipes: " + recipes);
                //System.out.println("Path: " + imgPath);
                loadRecipes(recipes, imgPath);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recyclerViewProductCategory);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeViewAdapter = new RecipeViewAdapter();
        recyclerView.setAdapter(recipeViewAdapter);
    }

    public void createRecipe(View view) {

        saveSelectedCategory(view.getId());

        Intent intent = new Intent(this, DisplayRecipe.class);


        startActivity(intent);

        System.out.println("Button pressed: " + view.getId());
    }

    private void loadRecipes(List recipes, List imgPath) {
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.recipe_item_view);

        //listView.setAdapter(adapter);

        System.out.println("Категория: " + recipes);
        recipeViewAdapter.clearItems();
        recipeViewAdapter.setItems(recipes, imgPath);
    }

    private void saveSelectedCategory(int recipe) {
        //int category = getIntent().getIntExtra("keyOfButton", 0);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("selected").child(androidId).child("recipe").setValue(recipe + 1);
    }
}