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
    int caloriesFrom;
    int caloriesTo;
    int radioButtonSelect;

    private HashMap<String, String> recipeValuesMap;
    private List<String> recipeValuesMapValues;
    String categoryString;
    String recipeString;
    String recipeName;
    String recipeCaloriesS;
    String recipeSteps;
    String recipeIngredients;
    Integer recipeCalories;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_filtered);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String caloriesFromS = getIntent().getStringExtra("caloriesFrom");
        String caloriesToS = getIntent().getStringExtra("caloriesTo");
        radioButtonSelect = getIntent().getIntExtra("radioButtonSelect", 0);

        try {
            caloriesFrom = Integer.parseInt(caloriesFromS);
        }
        catch (NumberFormatException ex) {
            caloriesFrom = 0;
        }

        try {
            caloriesTo = Integer.parseInt(caloriesToS);
        }
        catch (NumberFormatException ex) {
            caloriesTo = 10000;
        }


        System.out.println("caloriesFrom = " + caloriesFrom);
        System.out.println("caloriesTo = " + caloriesTo);

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        if (radioButtonSelect == 1) {
            path = "recipeFirstCourse";
            categoryString = ("category:" + radioButtonSelect);
        }
        if (radioButtonSelect == 2) {
            path = "recipeSecondCourse";
            categoryString = ("category:" + radioButtonSelect);
            dbRef.child("selected").child(androidId).child("category").setValue(2);
            //title = "Вторые блюда";
        }
        if (radioButtonSelect == 3) {
            path = "recipeSalad";
            categoryString = ("category:" + radioButtonSelect);
            dbRef.child("selected").child(androidId).child("category").setValue(3);
            //title = "Салаты";
        }
        if (radioButtonSelect == 4) {
            path = "recipeDessert";
            categoryString = ("category:" + radioButtonSelect);
            dbRef.child("selected").child(androidId).child("category").setValue(4);
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
                int numberOfRecipes = recipes.size();
                System.out.println("Кол-во рецептов: " + numberOfRecipes);
                int counterForRecipe = 0;
                int counterForArrayList = 0;
                while (numberOfRecipes > 0) {
                    recipeString = ("recipe:" + (counterForRecipe + 1));
                    GenericTypeIndicator<HashMap<String, String>> t1 = new GenericTypeIndicator<HashMap<String, String>>() {};
                    recipeValuesMap = dataSnapshot.child(categoryString).child(recipeString).getValue(t1);

                    System.out.println("Counter: " + counterForRecipe);
                    recipeValuesMapValues = new ArrayList<String>(recipeValuesMap.values());

                    recipeCaloriesS = recipeValuesMapValues.get(1);
                    recipeCalories = Integer.parseInt(recipeCaloriesS);
                    if (recipeCalories < caloriesFrom || recipeCalories > caloriesTo) {
                        recipes.remove(counterForArrayList);
                        recipesNumbers.remove(counterForArrayList);
                        counterForArrayList--;
                    }

                    counterForRecipe++;
                    counterForArrayList++;
                    numberOfRecipes--;
                }
                System.out.println("Фильтр   " + recipes);
                loadRecipes(recipes, recipesNumbers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recyclerViewProductCategoryFiltered);

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

        recipeViewAdapter.clearItems();
        recipeViewAdapter.setItems(recipes, imgPath);
    }

    private void saveSelectedCategory(int recipe) {
        //int category = getIntent().getIntExtra("keyOfButton", 0);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Integer a = recipesNumbers.get(recipe);
        dbRef.child("selected").child(androidId).child("recipe").setValue(a);
    }

}
