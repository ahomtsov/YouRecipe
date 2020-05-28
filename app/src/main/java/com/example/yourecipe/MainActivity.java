package com.example.yourecipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public int keyOfButton;
    int radioButtonSelect = 1;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void createRecyclerView(View view) {
        Intent intent = new Intent(this, DisplayRecyclerView.class);
        startActivity(intent);
    }

    public void createFirstCourseCategory(View view) {
        Intent intent = new Intent(this, DisplayRecipeCategory.class);

        keyOfButton = 1;

        intent.putExtra("keyOfButton", keyOfButton);
        saveSelectedCategory(keyOfButton);

        startActivity(intent);
    }

    public void createSecondCourseCategory(View view) {
        Intent intent = new Intent(this, DisplayRecipeCategory.class);

        keyOfButton = 2;

        intent.putExtra("keyOfButton", keyOfButton);
        saveSelectedCategory(keyOfButton);

        startActivity(intent);
    }

    public void createSaladCategory(View view) {
        Intent intent = new Intent(this, DisplayRecipeCategory.class);

        keyOfButton = 3;

        intent.putExtra("keyOfButton", keyOfButton);
        saveSelectedCategory(keyOfButton);

        startActivity(intent);
    }

    public void createDessertCategory(View view) {
        Intent intent = new Intent(this, DisplayRecipeCategory.class);

        keyOfButton = 4;

        intent.putExtra("keyOfButton", keyOfButton);
        saveSelectedCategory(keyOfButton);

        startActivity(intent);
    }

    public void createFiltered(View view) {
        Intent intent = new Intent(this, DisplayRecipeFiltered.class);

        EditText editCalFrom = (EditText)findViewById(R.id.editTextCaloriesFrom);
        EditText editCalTo = (EditText)findViewById(R.id.editTextCaloriesTo);
        String caloriesFrom = editCalFrom.getText().toString();
        String caloriesTo = editCalTo.getText().toString();


        //System.out.println("Calories" + caloriesFrom+"   "+caloriesTo);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupRecipeCategory);

        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(checkedRadioButtonId);

        String rbText = radioButton.getText().toString();

        System.out.println("RadioGroup: " + radioGroup);

        if (rbText.equals("Первые блюда"))
            radioButtonSelect = 1;

        if (rbText.equals("Вторые блюда"))
            radioButtonSelect = 2;

        if (rbText.equals("Салаты"))
            radioButtonSelect = 3;

        if (rbText.equals("Десерты"))
            radioButtonSelect = 4;

        System.out.println(checkedRadioButtonId);

        //2131230955 4
        //2131230956 1
        //2131230957 3
        //2131230958 2


        intent.putExtra("caloriesFrom", caloriesFrom);
        intent.putExtra("caloriesTo", caloriesTo);
        intent.putExtra("radioButtonSelect", radioButtonSelect);

        startActivity(intent);
    }

    private void saveSelectedCategory(int category) {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("selected").child(androidId).child("category").setValue(category);
    }
}
