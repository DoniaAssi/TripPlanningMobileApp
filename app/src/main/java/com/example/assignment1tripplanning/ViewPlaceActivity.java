package com.example.assignment1tripplanning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewPlaceActivity extends AppCompatActivity {

    ImageView imgCategory;
    TextView tvFromTo, tvCategory, tvDate, tvTime, tvImportant;
    Button btnEdit, btnDelete;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "places_pref";
    private static final String KEY_PLACES = "places_list";

    private ArrayList<Place> placeList = new ArrayList<>();
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        imgCategory = findViewById(R.id.imgCategory);
        tvFromTo = findViewById(R.id.tvFromTo);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvImportant = findViewById(R.id.tvImportant);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadPlaces();

        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String category = getIntent().getStringExtra("category");
        boolean important = getIntent().getBooleanExtra("important", false);

        index = getIntent().getIntExtra("index", -1);

        if (index == -1) {
            finish();
            return;
        }

        tvFromTo.setText(from + " → " + to);
        tvCategory.setText("Category: " + category);
        tvDate.setText("Date: " + date);
        tvTime.setText("Time: " + time);
        tvImportant.setText(important ? "Important ✓" : "Not Important");

        switch (category) {
            case "Beach":
                imgCategory.setImageResource(R.drawable.beach);
                break;
            case "City Tour":
                imgCategory.setImageResource(R.drawable.citytour);
                break;
            case "Adventure":
                imgCategory.setImageResource(R.drawable.adventure);
                break;
            default:
                imgCategory.setImageResource(R.drawable.historical);
        }

        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(ViewPlaceActivity.this, EditPlaceActivity.class);
            i.putExtra("index", index);
            startActivity(i);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            placeList.remove(index);
            savePlaces();
            finish();
        });
    }

    private void loadPlaces() {
        String json = prefs.getString(KEY_PLACES, "[]");
        Type type = new TypeToken<ArrayList<Place>>(){}.getType();
        placeList = new Gson().fromJson(json, type);
        if (placeList == null) placeList = new ArrayList<>();
    }

    private void savePlaces() {
        prefs.edit().putString(KEY_PLACES, new Gson().toJson(placeList)).apply();
    }
}
