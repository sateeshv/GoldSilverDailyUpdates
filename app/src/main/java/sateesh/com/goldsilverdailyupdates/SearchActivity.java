package sateesh.com.goldsilverdailyupdates;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import sateesh.com.goldsilverdailyupdates.Data.DatabaseContract;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    static String[] Month_Names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    TextView selectDate, emtpyDate;
    String userSelectedDate, userSelectedCity;
    Button selectDate_button, search_button;
    SearchPriceCursorAdapter cursorAdapter;
    SimpleCursorAdapter citySpinnerAdapter;
    String selectedCity;
    Spinner citySpinner;
    ListView listView;
    LinearLayout search_prams_titles;

    String[] cityFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        selectDate_button = (Button) findViewById(R.id.search_date_button);
        selectDate_button.setOnClickListener(this);

        selectDate = (TextView) findViewById(R.id.search_date_text_view);
        selectDate.setOnClickListener(this);

        search_button = (Button) findViewById(R.id.search_search_button);
        search_button.setOnClickListener(this);


        citySpinner = (Spinner) findViewById(R.id.search_city_spinner);
        String[] fromColumns = {DatabaseContract.CityInfo.COLUMN_CITY_NAME};
        int[] toColumns = {R.id.spinner_text_view};

        Uri city_uri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "13");
        Cursor cityQuery = getContentResolver().query(city_uri, null, null, null, null);
//        Log.v("Sateesh: ", "Spinner items in cursor: " + DatabaseUtils.dumpCursorToString(cityQuery));

        citySpinnerAdapter = new SimpleCursorAdapter(this, R.layout.city_spinner, cityQuery, fromColumns, toColumns, 0);
//            citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);
        int count = citySpinner.getCount();

        listView = (ListView) findViewById(R.id.search_list_items);
        cursorAdapter = new SearchPriceCursorAdapter(this, null, 0);
        listView.setAdapter(cursorAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_date_button:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.search_date_text_view:
                DialogFragment newFragment1 = new DatePickerFragment();
                newFragment1.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.search_search_button:
                if (userSelectedDate != null) {
                    TextView selectedTextView = (TextView) findViewById(R.id.spinner_text_view); // You may need to replace android.R.id.text1 whatever your TextView label id is
                    userSelectedCity = selectedTextView.getText().toString();

                    Log.v("Sateesh: ", "*** User selected City is:" + userSelectedCity);

                    cityFilter = new String[1];
                    cityFilter[0] = userSelectedCity;
                    Log.v("Sateesh: ", "CityFilter option value is: " + cityFilter[0].toString());
                    getLoaderManager().restartLoader(1, null, this);

                } else {
                    emtpyDate = (TextView) findViewById(R.id.search_error_message);
                    emtpyDate.setText("Please select a valid date");
                }
        }
    }


    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //populateSetDate(year, month, day);
            TextView startDate = (TextView) getActivity().findViewById(R.id.search_date_text_view);
            String StartDate_Month_Name = Month_Names[month];
            String formattedMonth;
            String formattedDay;
            if (month < 10) {
                formattedMonth = "0" + (month + 1);
            } else {
                formattedMonth = String.valueOf(month + 1);
            }
            if (day < 10) {
                formattedDay = "0" + day;
            } else {
                formattedDay = String.valueOf(day);
            }


//            userSelectedDate = year + "-" + formattedMonth + "-" + formattedDay;
            userSelectedDate = formattedDay + "-" + Month_Names[(Integer.parseInt(formattedMonth)-1)] + "-" + year;
            Log.v("Sateesh: ", "User selected Date: " + userSelectedDate);
            String formattedValues = day + " - " + StartDate_Month_Name + " - " + year;
            startDate.setText(formattedValues);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "15");
//        Uri uri = DatabaseContract.ClassInfo.CONTENT_URI;
        Log.v("Sateesh: ", "*** onCreateLoader reached");
//        String citySelection = DatabaseContract.PriceInfo.COLUMN_CITY_NAME + "=?";
//        String[] filter = {"Gudur", "Nellore", "Hyderabad"};
        String[] filter = cityFilter;
//        Log.v("Sateesh: ", "*** Selection Param is: " + citySelection);
//        Log.v("Sateesh: ", "*** Selection Param value is: " + filter);
//        return new CursorLoader(this, uri, null, citySelection, filter, null);

        String whereCondition = DatabaseContract.PriceInfo.COLUMN_DATE + " = " + '"' + userSelectedDate + '"' + " AND " +
                DatabaseContract.PriceInfo.COLUMN_CITY_NAME + " = " + '"' + userSelectedCity + '"';
        return new CursorLoader(this, uri, null, whereCondition, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Sateesh: ", "*** onLoadFinished reached");
        Log.v("Sateesh: ", "*** Cursor Data in onLoadFinised: " + DatabaseUtils.dumpCursorToString(data));
        search_prams_titles = (LinearLayout) findViewById(R.id.search_params);
        if (data.getCount() > 0 && data != null) {

            search_prams_titles.setVisibility(View.VISIBLE);
            cursorAdapter.swapCursor(data);
        } else {

            TextView empty = (TextView) findViewById(R.id.emptyListElem);
            listView.setEmptyView(empty);
            search_prams_titles.setVisibility(View.GONE);

            cursorAdapter.swapCursor(null);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}

