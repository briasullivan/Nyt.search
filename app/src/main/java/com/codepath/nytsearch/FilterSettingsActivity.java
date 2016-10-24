package com.codepath.nytsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

public class FilterSettingsActivity extends AppCompatActivity {

    private DatePicker beginDatePicker;
    private Spinner sortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);
        setupViews();
    }

    private void setupViews() {
        beginDatePicker = (DatePicker) findViewById(R.id.beginDatePicker);
        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> sortAdapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.sort_array,
                        android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
    }

}
