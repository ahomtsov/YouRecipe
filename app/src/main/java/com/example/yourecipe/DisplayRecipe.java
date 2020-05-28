package com.example.yourecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

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


public class DisplayRecipe extends AppCompatActivity {

    private DatabaseReference dbRef;
    private StorageReference mStorageRef;
    private HashMap<String, Integer> categoryAndRecipeNumberMap;
    private HashMap<String, String> recipeValuesMap;
    private List<Integer> categoryAndRecipeNumber;
    private List<String> recipeValuesMapValues;
    private Integer categoryNumber;
    private Integer recipeNumber;
    String deviceId;
    String title;
    String categoryString;
    String recipeString;
    String recipeName;
    String recipeCalories;
    String recipeSteps;
    String recipeIngredients;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                GenericTypeIndicator<HashMap<String, Integer>> t = new GenericTypeIndicator<HashMap<String, Integer>>() {};
                categoryAndRecipeNumberMap = dataSnapshot.child("selected").child(deviceId).getValue(t);
                categoryAndRecipeNumber = new ArrayList<Integer>(categoryAndRecipeNumberMap.values());

                categoryNumber = categoryAndRecipeNumber.get(1);
                recipeNumber = categoryAndRecipeNumber.get(0);

                categoryString = ("category:" + categoryNumber);
                recipeString = ("recipe:" + recipeNumber);

                GenericTypeIndicator<HashMap<String, String>> t1 = new GenericTypeIndicator<HashMap<String, String>>() {};
                recipeValuesMap = dataSnapshot.child(categoryString).child(recipeString).getValue(t1);

                recipeValuesMapValues = new ArrayList<String>(recipeValuesMap.values());

                recipeName = recipeValuesMapValues.get(0);
                recipeCalories = recipeValuesMapValues.get(1);
                recipeSteps = recipeValuesMapValues.get(2);
                recipeIngredients = recipeValuesMapValues.get(3);

                TextView name = findViewById(R.id.textViewRecipeName);
                TextView ingredients = findViewById(R.id.textViewIngredientsList);
                TextView calories = findViewById(R.id.textViewCalories);
                TextView steps = findViewById(R.id.textViewSteps);

                name.setText(recipeName);
                ingredients.setText(recipeIngredients);
                steps.setText(recipeSteps);
                calories.setText(recipeCalories);

                //System.out.println("Recipe: " + recipeName + "  " + recipeCalories+ "  " + recipeSteps+ "  " + recipeIngredients);

                //products.remove(0);
                //System.out.println("Recipes: " + categoryNumber + "   " + recipeNumber);
                //System.out.println("Path: " + imgPath);
                //loadRecipes(recipes, imgPath);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
