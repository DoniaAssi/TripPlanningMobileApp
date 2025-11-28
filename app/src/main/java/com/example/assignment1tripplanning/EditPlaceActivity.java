package com.example.assignment1tripplanning;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EditPlaceActivity extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerTo;
    private RadioGroup radioGroupCategory;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox checkImportant;
    private Button btnUpdate, btnDelete;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "places_pref";
    private static final String KEY_PLACES = "places_list";

    private ArrayList<Place> placeList = new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        radioGroupCategory = findViewById(R.id.radioGroupCategory);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        checkImportant = findViewById(R.id.checkImportant);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadPlaces();

        String[] cities = {"Ramallah", "Jericho", "Nablus", "Hebron", "Bethlehem", "Jenin", "Tulkarm", "Qalqilya", "Gaza"};
        spinnerFrom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities));
        spinnerTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities));

        position = getIntent().getIntExtra("position", -1);
        if (position == -1) { finish(); return; }

        Place p = placeList.get(position);

        spinnerFrom.setSelection(getIndex(spinnerFrom, p.getFrom()));
        spinnerTo.setSelection(getIndex(spinnerTo, p.getTo()));

        selectRadio(p.getCategory());

        String[] dp = p.getDate().split("-");
        datePicker.updateDate(Integer.parseInt(dp[0]), Integer.parseInt(dp[1]) - 1, Integer.parseInt(dp[2]));

        String[] tp = p.getTime().split(":");
        timePicker.setHour(Integer.parseInt(tp[0]));
        timePicker.setMinute(Integer.parseInt(tp[1]));

        checkImportant.setChecked(p.isImportant());

        btnUpdate.setOnClickListener(v -> updatePlace());
        btnDelete.setOnClickListener(v -> deletePlace());
    }

    private void selectRadio(String category) {
        if (category.equals("Beach")) {
            ((RadioButton) findViewById(R.id.radioBeach)).setChecked(true);
        } else if (category.equals("City Tour")) {
            ((RadioButton) findViewById(R.id.radioCityTour)).setChecked(true);
        } else if (category.equals("Adventure")) {
            ((RadioButton) findViewById(R.id.radioAdventure)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.radioHistorical)).setChecked(true);
        }
    }

    private int getIndex(Spinner s, String value) {
        for (int i = 0; i < s.getCount(); i++)
            if (s.getItemAtPosition(i).toString().equals(value))
                return i;
        return 0;
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

    private void updatePlace() {

        int radioId = radioGroupCategory.getCheckedRadioButtonId();
        RadioButton selected = findViewById(radioId);

        placeList.set(position,
                new Place(
                        spinnerFrom.getSelectedItem().toString(),
                        spinnerTo.getSelectedItem().toString(),
                        datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth(),
                        timePicker.getHour() + ":" + timePicker.getMinute(),
                        selected.getText().toString(),
                        checkImportant.isChecked()
                )
        );

        savePlaces();
        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deletePlace() {
        placeList.remove(position);
        savePlaces();
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
