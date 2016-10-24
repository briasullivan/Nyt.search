package com.codepath.nytsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

public class FilterSettingsActivity extends AppCompatActivity {

    private DatePicker beginDatePicker;
    private Spinner sortSpinner;
    private CheckBox artCheckbox;
    private CheckBox blogsCheckbox;
    private CheckBox educationCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);
        setupViews();
    }

    private void setupViews() {
        beginDatePicker = (DatePicker) findViewById(R.id.beginDatePicker);
        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        artCheckbox = (CheckBox) findViewById(R.id.cbArt);
        blogsCheckbox = (CheckBox) findViewById(R.id.cbBlogs);
        educationCheckbox = (CheckBox) findViewById(R.id.cbEducation);

        ArrayAdapter<CharSequence> sortAdapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.sort_array,
                        android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        Bundle extras = getIntent().getExtras();
        String date = extras.getString("beginDate");
        String newsDeskValues = extras.getString("newsDeskValues");
        Log.d("DEBUG", "date value: " + date);
        if (date != null) {
            int year = Integer.valueOf(date.substring(0, 4));
            int month = Integer.valueOf(date.substring(4, 6)) - 1;
            int day = Integer.valueOf(date.substring(6, 8));

            beginDatePicker.updateDate(year, month, day);
        }
        if (newsDeskValues != null) {
            artCheckbox.setChecked(newsDeskValues.contains(artCheckbox.getText()));
            blogsCheckbox.setChecked(newsDeskValues.contains(blogsCheckbox.getText()));
            educationCheckbox.setChecked(newsDeskValues.contains(educationCheckbox.getText()));
        }
        int sortPosition = extras.getInt("sortType");
        sortSpinner.setSelection(sortPosition);
    }

    public void applyFilters(View view) {
        Intent data = new Intent();
        data.putExtra("beginDate",
                        beginDatePicker.getYear() +
                        numToString(beginDatePicker.getMonth() + 1) +
                        numToString(beginDatePicker.getDayOfMonth()));
        data.putExtra("sortType", sortSpinner.getSelectedItemPosition());

        String newsDesk = "";
        if (artCheckbox.isChecked()) {
            newsDesk += "\"" + artCheckbox.getText() + "\" ";
        }
        if (blogsCheckbox.isChecked()) {
            newsDesk += "\"" + blogsCheckbox.getText() + "\" ";
        }
        if (educationCheckbox.isChecked()) {
            newsDesk += "\"" + educationCheckbox.getText() + "\" ";
        }

        if (newsDesk.length() > 0) {
            data.putExtra("newsDeskValues", newsDesk);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private String numToString(int dateNum) {
        return (dateNum < 10) ? "0" + dateNum : String.valueOf(dateNum);
    }
}
