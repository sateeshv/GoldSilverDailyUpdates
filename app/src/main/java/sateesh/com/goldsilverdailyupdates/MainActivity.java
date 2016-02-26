package sateesh.com.goldsilverdailyupdates;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sateesh.com.goldsilverdailyupdates.Data.DatabaseContract;
import sateesh.com.goldsilverdailyupdates.Network.ExitNoInternet;
import sateesh.com.goldsilverdailyupdates.Network.ExitWithInternet;
import sateesh.com.goldsilverdailyupdates.Network.InternetCheck;
import sateesh.com.goldsilverdailyupdates.Network.LaunchNoInternet;
import sateesh.com.goldsilverdailyupdates.Notifications.AlarmReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String currentSystemDate;
    String IPAddress = "192.168.1.113";
    String getLatestPriceData_URL = "http://" + IPAddress + "/gold_smart_updates/show_All_Prices_Above_TimeStamp.php";
    String getAllPriceData_URL = "http://" + IPAddress + "/gold_smart_updates/show_All_Prices.php";
    String getLatestCity_URL = "http://" + IPAddress + "/gold_smart_updates/showCities_Above_TimeStamp.php";
    String getAllCities_URL = "http://" + IPAddress + "/gold_smart_updates/showCities.php";
    RequestQueue requestQueue;
    ImageButton gold_icon, silver_icon, chart_icon, search_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        gold_icon = (ImageButton) findViewById(R.id.gold_image);
        gold_icon.setOnClickListener(this);

        silver_icon = (ImageButton) findViewById(R.id.silver_image);
        silver_icon.setOnClickListener(this);

        chart_icon = (ImageButton) findViewById(R.id.chart_image);
        chart_icon.setOnClickListener(this);

        search_icon = (ImageButton) findViewById(R.id.search_image);
        search_icon.setOnClickListener(this);

        onLaunchCode();
        triggerNotification();

    }

    public void onLaunchCode() {
        Uri cityUri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "14");
        Log.v("Sateesh: ", "*** URI link is: " + cityUri);
        Cursor cityLastRecord = getContentResolver().query(cityUri, null, null, null, null);
        Log.v("Sateesh: ", "*** are there any records: " + (cityLastRecord != null ? cityLastRecord.getCount() : 0));

        String cityLastInsertedDate = null;

        try {
            if (cityLastRecord != null && cityLastRecord.getCount() > 0) {
                cityLastRecord.moveToFirst();
                cityLastInsertedDate = cityLastRecord.getString(cityLastRecord.getColumnIndexOrThrow(DatabaseContract.CityInfo.COLUMN_MODIFIED_DATETIME));
                Log.v("Sateesh: ", "*** Last Inserted City is: " + cityLastInsertedDate);
                getCityData_method(getLatestCity_URL, cityLastInsertedDate);

            } else {
                Log.v("Sateesh: ", "*** No City insertions till Now");
                getCityData_method(getAllCities_URL, "");
            }

        } catch (CursorIndexOutOfBoundsException e) {
            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
        }

        Uri priceUri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "2");
        Log.v("Sateesh: ", "*** URI link is: " + priceUri);
        Cursor priceLastRecord = getContentResolver().query(priceUri, null, null, null, null);
        Log.v("Sateesh: ", "*** are there any records: " + (priceLastRecord != null ? priceLastRecord.getCount() : 0));

        String priceLastInsertedDate = null;

        try {
            if (priceLastRecord != null && priceLastRecord.getCount() > 0) {
                priceLastRecord.moveToFirst();
                priceLastInsertedDate = priceLastRecord.getString(priceLastRecord.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME));
                Log.v("Sateesh: ", "*** Last Inserted Date is: " + priceLastInsertedDate);
                getPriceData_method(getLatestPriceData_URL, priceLastInsertedDate);
            } else {
                Log.v("Sateesh: ", "*** No insertions till Now");
                getPriceData_method(getAllPriceData_URL, "");
            }

        } catch (CursorIndexOutOfBoundsException e) {
            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
        }


    }

    public void getPriceData_method(String PriceDataURL, final String lastRecordDate) {
        Log.v("Sateesh ", "*** ShowData started working");


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                PriceDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Sateesh ", "*** Response is working");
                Log.v("Sateesh ", "*** Response is working" + response);
                try {

                    List<ContentValues> storeData = new ArrayList<ContentValues>();

                    JSONObject data = new JSONObject(response);
                    JSONArray priceData = data.getJSONArray("Cities");
                    Log.v("Sateesh ", "*** JSON Array data is :" + priceData);
                    Log.v("Sateesh ", "*** JSON Array Length is :" + priceData.length());
                    for (int i = 0; i < priceData.length(); i++) {
                        JSONObject price = priceData.getJSONObject(i);

                        String PriceDate = price.getString("PriceDate");
                        String City = price.getString("City");
                        int Gold_22ct_1_gram = price.getInt("Gold_22ct_1_gram");
                        int Silver_1_gram = price.getInt("Silver_1_gram");
                        String gold_change = price.getString("Gold_Change");
                        String silver_change = price.getString("Silver_Change");

                        Log.v("Sateesh", "*** From getAllData_method: Date is: " + PriceDate);
                        Log.v("Sateesh", "*** From getAllData_method: City is: " + City);
                        Log.v("Sateesh", "*** From getAllData_method: Yesterday Gold 1 Gram is : " + String.valueOf(Gold_22ct_1_gram));
                        Log.v("Sateesh", "*** From getAllData_method: Yesterday Silver 1 Gram is : " + String.valueOf(Silver_1_gram));
                        Log.v("Sateesh", "*** From getAllData_method: GOLD_Change is: " + gold_change);
                        Log.v("Sateesh", "*** From getAllData_method: Silver_Change is: " + silver_change);

                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME, timeStamp);
                        values.put(DatabaseContract.PriceInfo.COLUMN_DATE, PriceDate);
                        values.put(DatabaseContract.PriceInfo.COLUMN_CITY_NAME, City);
                        values.put(DatabaseContract.PriceInfo.COLUMN_GOLD_1_GM, Gold_22ct_1_gram);
                        values.put(DatabaseContract.PriceInfo.COLUMN_SILVER_1_GM, Silver_1_gram);
                        values.put(DatabaseContract.PriceInfo.COLUMN_GOLD_CHANGE, gold_change);
                        values.put(DatabaseContract.PriceInfo.COLUMN_SILVER_CHANGE, silver_change);

                        if (storeData != null) {
                            storeData.add(values);
                        }
                    }
                    if (storeData.size() > 0) {
                        Log.v("Sateesh: ", "*** getAllData_method Prices Records count is: " + storeData.size());
                        ContentValues[] dataArray = new ContentValues[storeData.size()];
                        ContentValues[] values = storeData.toArray(dataArray);
                        Log.v("Sateesh: ", "**** getAllData_method content Values data " + values);

                        Uri data_uri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "0");
                        int insertedRecords = getContentResolver().bulkInsert(data_uri, dataArray);
                        Log.v("Sateesh: ", "*** MainActivity getAllData_method + Data Inserted Records: " + insertedRecords);
                    } else {
                        Log.v("Sateesh", "NO New Data Available");
                    }
                } catch (JSONException e1) {
                    Log.v("Sateesh ", "**** Error is " + e1.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Sateesh ", "**** Error is " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Modified_DateTime", lastRecordDate);
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getCityData_method(String CityDataURL, final String lastRecordDate) {
        Log.v("Sateesh ", "*** ShowData started working");


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                CityDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Sateesh ", "*** Response is working");
                Log.v("Sateesh ", "*** Response is working" + response);
                try {

                    List<ContentValues> storeData = new ArrayList<ContentValues>();

                    JSONObject data = new JSONObject(response);
                    JSONArray priceData = data.getJSONArray("Cities");
                    Log.v("Sateesh ", "*** JSON Array data is :" + priceData);
                    Log.v("Sateesh ", "*** JSON Array Length is :" + priceData.length());
                    for (int i = 0; i < priceData.length(); i++) {
                        JSONObject price = priceData.getJSONObject(i);


                        String City = price.getString("CityName");


                        Log.v("Sateesh", "*** From getAllData_method: City is: " + City);

                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME, timeStamp);
                        values.put(DatabaseContract.PriceInfo.COLUMN_CITY_NAME, City);

                        if (storeData != null) {
                            storeData.add(values);
                        }
                    }
                    if (storeData.size() > 0) {
                        ContentValues[] cityDataArray = new ContentValues[storeData.size()];
                        ContentValues[] cityValues = storeData.toArray(cityDataArray);
                        Log.v("Sateesh: ", "**** City Values data " + Arrays.toString(cityDataArray));

                        Uri city_uri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "1");
                        int insertedRecords = getApplication().getContentResolver().bulkInsert(city_uri, cityDataArray);
                        Log.v("Sateesh: ", "*** FetchPricesTask + City Inserted Records: " + insertedRecords);
                    } else {
                        Log.v("Sateesh", "NO New Data Available");
                    }
                } catch (JSONException e1) {
                    Log.v("Sateesh ", "**** Error is " + e1.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Sateesh ", "**** Error is " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Modified_DateTime", lastRecordDate);
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gold_image:
                Intent data_gold = new Intent(MainActivity.this, DataActivity.class);
                startActivity(data_gold);
                break;

            case R.id.silver_image:
                Intent data_silver = new Intent(MainActivity.this, DataSilver.class);
                startActivity(data_silver);

                break;

            case R.id.chart_image:
                Intent charts = new Intent(MainActivity.this, ChartsActivity.class);
                startActivity(charts);
                break;

            case R.id.search_image:
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
                break;

        }

    }

    public void triggerNotification() {
        Intent myIntent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 13); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) // you can add buffer time too here to ignore some small differences in milliseconds
        {
            //set from today
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    intendedTime, AlarmManager.INTERVAL_DAY,
                    pendingIntent);

        } else {
            //set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    intendedTime, AlarmManager.INTERVAL_DAY,
                    pendingIntent);

        }
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), 5 * 1000, pendingIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (InternetCheck.isConnectingToInternet(this) == true) {
        } else {

            LaunchNoInternet noInternet = new LaunchNoInternet();
            noInternet.show(getFragmentManager(), "LaunchNoInternet");

        }
    }

    @Override
    public void onBackPressed() {
        if (InternetCheck.isConnectingToInternet(this) == true) {

            ExitWithInternet exitWithInternet = new ExitWithInternet();
            exitWithInternet.show(getFragmentManager(), "ExitWithInternet");

        } else {

            ExitNoInternet exitNoInternet = new ExitNoInternet();
            exitNoInternet.show(getFragmentManager(), "ExitNoInternet");
        }
    }

}
