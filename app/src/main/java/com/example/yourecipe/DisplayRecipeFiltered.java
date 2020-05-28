package com.example.yourecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.yourecipe.adapter.RecipeViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayRecipeFiltered extends AppCompatActivity {

    private DatabaseReference dbRef;
    private StorageReference mStorageRef;
    private HashMap<String, Integer> recipeMap;
    private List<String> recipes;
    private List<Integer> recipesNumbers;
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

        String caloriesFrom = getIntent().getStringExtra("caloriesFrom");
        String caloriesTo = getIntent().getStringExtra("caloriesTo");
        int radioButtonSelect = getIntent().getIntExtra("radioButtonSelect", 0);

        if (radioButtonSelect == 1) {
            path = "recipeFirstCourse";
            //title = "Первые блюда";
        }
        if (radioButtonSelect == 2) {
            path = "recipeSecondCourse";
            //title = "Вторые блюда";
        }
        if (radioButtonSelect == 3) {
            path = "recipeSalad";
            //title = "Салаты";
        }
        if (radioButtonSelect == 4) {
            path = "recipeDessert";
            //title = "Десерты";
        }

        System.out.println("RadioButton = " + radioButtonSelect);

        //setTitle(title);

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Integer>> t = new GenericTypeIndicator<HashMap<String, Integer>>() {};
                recipeMap = dataSnapshot.child(path).getValue(t);
                recipes = new ArrayList<String>(recipeMap.keySet());
                recipesNumbers = new ArrayList<Integer>(recipeMap.values());
                //products.remove(0);
                System.out.println("Recipes: " + recipes);
                System.out.println("Номер рецепта: " + recipesNumbers);
                loadRecipes(recipes, recipesNumbers);
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

        //saveSelectedCategory(view.getId());

        Intent intent = new Intent(this, DisplayRecipe.class);


        startActivity(intent);

        System.out.println("Button pressed: " + view.getId());
    }

    private void loadRecipes(List recipes, List imgPath) {
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.recipe_item_view);

        //listView.setAdapter(adapter);


        recipeViewAdapter.setItems(recipes, imgPath);
    }

}
