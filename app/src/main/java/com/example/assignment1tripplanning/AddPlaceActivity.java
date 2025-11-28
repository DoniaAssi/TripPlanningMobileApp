package com.example.assignment1tripplanning;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerTo;
    private RadioGroup radioGroupCategory;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox checkImportant;
    private Button btnSave;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "places_pref";
    private static final String KEY_PLACES = "places_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        radioGroupCategory = findViewById(R.id.radioGroupCategory);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        checkImportant = findViewById(R.id.checkImportant);
        btnSave = findViewById(R.id.btnSave);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String[] cities = {"Ramallah", "Jericho", "Nablus", "Hebron", "Bethlehem", "Jenin", "Tulkarm", "Qalqilya", "Gaza"};
        spinnerFrom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities));
        spinnerTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities));

        btnSave.setOnClickListener(v -> savePlace());
    }

    private void savePlace() {

        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        int radioId = radioGroupCategory.getCheckedRadioButtonId();
        if (radioId == -1) {
            Toast.makeText(this, "Select trip category", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selected = findViewById(radioId);
        String category = selected.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = year + "-" + month + "-" + day;

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String time = hour + ":" + minute;

        boolean important = checkImportant.isChecked();

        String json = prefs.getString(KEY_PLACES, "[]");
        Type type = new TypeToken<ArrayList<Place>>(){}.getType();
        ArrayList<Place> list = new Gson().fromJson(json, type);

        if (list == null) list = new ArrayList<>();

        list.add(new Place(from, to, date, time, category, important));

        prefs.edit().putString(KEY_PLACES, new Gson().toJson(list)).apply();

        Toast.makeText(this, "Trip Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
