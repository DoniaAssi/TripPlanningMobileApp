package com.example.assignment1tripplanning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPlaces;
    private EditText edtSearch;
    private Button btnAdd;

    private ArrayList<Place> placeList = new ArrayList<>();
    private PlaceAdapter adapter;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "places_pref";
    private static final String KEY_PLACES = "places_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPlaces = findViewById(R.id.recyclerPlaces);
        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadPlaces();

        recyclerPlaces.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter(placeList);
        recyclerPlaces.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddPlaceActivity.class))
        );

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterPlaces(s.toString());
            }
        });
    }

    private void loadPlaces() {
        Gson gson = new Gson();
        String json = prefs.getString(KEY_PLACES, "[]");
        Type type = new TypeToken<ArrayList<Place>>(){}.getType();
        placeList = gson.fromJson(json, type);

        if (placeList == null)
            placeList = new ArrayList<>();
    }

    private void filterPlaces(String text) {
        ArrayList<Place> filtered = new ArrayList<>();

        for (Place p : placeList) {
            if (p.getFrom().toLowerCase().contains(text.toLowerCase()) ||
                    p.getTo().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(p);
            }
        }

        adapter = new PlaceAdapter(filtered);
        recyclerPlaces.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlaces();
        adapter = new PlaceAdapter(placeList);
        recyclerPlaces.setAdapter(adapter);
    }
}
