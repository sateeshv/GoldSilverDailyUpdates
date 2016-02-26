package sateesh.com.goldsilverdailyupdates;


import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import sateesh.com.goldsilverdailyupdates.Data.DatabaseContract;


public class DataGold extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_SECTION_NUMBER = "section_number";


    GoldPriceCursorAdapter cursorAdapter;
    SimpleCursorAdapter citySpinnerAdapter;
    String selectedCity;

    String[] cityFilter;

    public static DataGold newInstance(int sectionNumber) {
        DataGold fragment = new DataGold();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_gold, container, false);

        final Spinner citySpinner = (Spinner) v.findViewById(R.id.data_gold_city_spinner);
        String[] fromColumns = {DatabaseContract.CityInfo.COLUMN_CITY_NAME};
        int[] toColumns = {R.id.spinner_text_view};

        Uri city_uri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "13");
        Cursor cityQuery = getActivity().getContentResolver().query(city_uri, null, null, null, null);
        Log.v("Sateesh: ", "Spinner items in cursor: " + DatabaseUtils.dumpCursorToString(cityQuery));

        citySpinnerAdapter = new SimpleCursorAdapter(getContext(), R.layout.city_spinner, cityQuery, fromColumns, toColumns, 0);
//            citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citySpinnerAdapter);
        int count = citySpinner.getCount();

        ListView listView = (ListView) v.findViewById(R.id.gold_list_items);
        cursorAdapter = new GoldPriceCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(cursorAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int location = citySpinner.getSelectedItemPosition();
                String s = citySpinner.getAdapter().getItem(position).toString();

                if (citySpinner != null && citySpinner.getChildAt(0) != null) {
                    TextView selectedTextView = (TextView) view.findViewById(R.id.spinner_text_view); // You may need to replace android.R.id.text1 whatever your TextView label id is
                    selectedCity = selectedTextView.getText().toString();
                    Log.v("Sateesh: ", "*** Selected option is: " + selectedCity);
                    Log.v("Sateesh: ", "*** Position is: " + position);

                    cityFilter = new String[1];
                    cityFilter[0] = selectedCity;
                    Log.v("Sateesh: ", "CityFilter option value is: " + cityFilter[0].toString());
                    getLoaderManager().restartLoader(position, null, DataGold.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int selectedItemId = (int) citySpinner.getSelectedItemPosition();
        Log.v("Sateesh: ", "*** selectedItem is: " + selectedItemId);


        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "3");
//        Uri uri = DatabaseContract.ClassInfo.CONTENT_URI;
        Log.v("Sateesh: ", "*** onCreateLoader reached");
        String citySelection = DatabaseContract.PriceInfo.COLUMN_CITY_NAME + "=?";
//        String[] filter = {"Gudur", "Nellore", "Hyderabad"};
        String[] filter = cityFilter;
        Log.v("Sateesh: ", "*** Selection Param is: " + citySelection);
        Log.v("Sateesh: ", "*** Selection Param value is: " + filter);
        return new CursorLoader(getActivity(), uri, null, citySelection, filter, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Sateesh: ", "*** onLoadFinished reached");
        Log.v("Sateesh: ", "*** Cursor Data in onLoadFinised: " + DatabaseUtils.dumpCursorToString(data));
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}

