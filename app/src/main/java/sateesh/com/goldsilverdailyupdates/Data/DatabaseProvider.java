package sateesh.com.goldsilverdailyupdates.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

public class DatabaseProvider extends ContentProvider {

    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    public static final String LOG_CAT = "Sateesh";

    public static final int INSERT_PRICES = 0;
    public static final int INSERT_CITIES = 1;

    public static final int QUERY_LAST_RECORD = 2;

    public static final int QUERY_CITY_GOLD_7_DAYS = 3;
    public static final int QUERY_CITY_GOLD_LAST_30_DAYS = 4;
    public static final int QUERY_CITY_GOLD_LAST_90_DAYS = 5;
    public static final int QUERY_CITY_GOLD_LAST_12_MONTHS = 6;

    public static final int QUERY_CITY_SILVER_7_DAYS = 7;
    public static final int QUERY_CITY_SILVER_LAST_30_DAYS = 8;
    public static final int QUERY_CITY_SILVER_LAST_90_DAYS = 9;
    public static final int QUERY_CITY_SILVER_LAST_12_MONTHS = 10;

    public static final int DELETE_PRICES = 11;
    public static final int DELETE_CITIES = 12;

    public static final int QUERY_CITY = 13;
    public static final int QUERY_CITY_LAST_RECORD = 14;

    public static final int QUERY_DATE_CITY = 15;



    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/0", INSERT_PRICES);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_CITY_INFO + "/1", INSERT_CITIES);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/2", QUERY_LAST_RECORD);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/3", QUERY_CITY_GOLD_7_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/4", QUERY_CITY_GOLD_LAST_30_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/5", QUERY_CITY_GOLD_LAST_90_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/6", QUERY_CITY_GOLD_LAST_12_MONTHS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/7", QUERY_CITY_SILVER_7_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/8", QUERY_CITY_SILVER_LAST_30_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/9", QUERY_CITY_SILVER_LAST_90_DAYS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/10", QUERY_CITY_SILVER_LAST_12_MONTHS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/11", DELETE_PRICES);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_CITY_INFO + "/12", DELETE_CITIES);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_CITY_INFO + "/13", QUERY_CITY);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_CITY_INFO + "/14", QUERY_CITY_LAST_RECORD);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_PRICE_INFO + "/15", QUERY_DATE_CITY);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursorData = null;
        int match = matcher.match(uri);

        switch (match) {
            case QUERY_LAST_RECORD:
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.PriceInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME + " DESC ", " 1");
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_GOLD_7_DAYS:
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.PriceInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.PriceInfo.COLUMN_DATE + " DESC ", " 7");
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);

                break;

            case QUERY_CITY_GOLD_LAST_30_DAYS:
//                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.PriceInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.PriceInfo.COLUMN_DATE + " DESC ", " 30");
//                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                String query_gold_30_days = "SELECT * from (SELECT PriceDate , Gold1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 30) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_gold_30_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_GOLD_LAST_90_DAYS:
                String query_gold_90_days = "SELECT * from (SELECT PriceDate , Gold1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 90) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_gold_90_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_GOLD_LAST_12_MONTHS:
                String query_gold_365_days = "SELECT * from (SELECT PriceDate , Gold1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 90) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_gold_365_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_SILVER_7_DAYS:
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.PriceInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.PriceInfo.COLUMN_DATE + " DESC ", " 7");
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_SILVER_LAST_30_DAYS:
                String query_silver_30_days = "SELECT * from (SELECT PriceDate , Silver1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 30) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_silver_30_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_SILVER_LAST_90_DAYS:
                String query_silver_90_days = "SELECT * from (SELECT PriceDate , Silver1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 90) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_silver_90_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_SILVER_LAST_12_MONTHS:
                String query_silver_365_days = "SELECT * from (SELECT PriceDate , Silver1Gram from PriceInfo Where " + selection + " order by PriceDate desc limit 365) ORDER BY PriceDate ASC";
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query_silver_365_days, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY:
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.CityInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null, null);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case QUERY_CITY_LAST_RECORD:
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.CityInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.CityInfo.COLUMN_MODIFIED_DATETIME + " DESC ", " 1");
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;


            case QUERY_DATE_CITY:
                String query = "SELECT * FROM PriceInfo where " + selection;
                Log.v("Sateesh: ", "*** RawQuery string is : " + query);
                cursorData = databaseHelper.getReadableDatabase().rawQuery(query, selectionArgs);
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursorData;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase writeToDatabase = databaseHelper.getWritableDatabase();
        int insertedRecords = 0;
        final int match = matcher.match(uri);
        switch (match) {
            case INSERT_CITIES:
                writeToDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        Log.v("Sateesh", "*** Bulk Insert city value is:  " + value.toString());
                        long insertRecord = writeToDatabase.insert(DatabaseContract.CityInfo.TABLE_NAME, null, value);
                        Log.v("Sateesh", "*** InsertRecord value is: " + insertRecord);
                        if (insertRecord > 0) {
                            ContentUris.withAppendedId(uri, insertRecord);
                            Log.v(LOG_CAT, "*** Inserted City Record is: " + ContentUris.withAppendedId(uri, insertRecord));
                            insertedRecords++;
                        } else {
                            Log.v(LOG_CAT, "*** No City records to Insert");
                        }
                    }
                    writeToDatabase.setTransactionSuccessful();
                } finally {
                    writeToDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case INSERT_PRICES:
                writeToDatabase.beginTransaction();
                try {
                    for (ContentValues records : values) {
                        long insertRecord = writeToDatabase.insert(DatabaseContract.PriceInfo.TABLE_NAME, null, records);
                        if (insertRecord > 0) {
                            ContentUris.withAppendedId(uri, insertRecord);
                            Log.v(LOG_CAT, "*** Inserted Record is: " + ContentUris.withAppendedId(uri, insertRecord));
                            insertedRecords++;
                        } else {
                            Log.v(LOG_CAT, "*** No records to Insert");
                        }

                    }
                    writeToDatabase.setTransactionSuccessful();
                } finally {
                    writeToDatabase.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                return super.bulkInsert(uri, values);
        }
        Log.v("Sateesh: ", "*** Total Number of Price records Inserted: " + insertedRecords);
        return insertedRecords;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = matcher.match(uri);
        int delete = 0;
        switch (match) {
            case DELETE_PRICES:
                delete = databaseHelper.getWritableDatabase().delete(DatabaseContract.PriceInfo.TABLE_NAME, null, null);
                if (delete > 0) {
                    ContentUris.withAppendedId(uri, delete);
                    Log.v("Sateesh: ", "**** Data records deleted " + delete);
                } else {
                    try {
                        throw new SQLException("No records to Delete" + uri);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case DELETE_CITIES:
                delete = databaseHelper.getWritableDatabase().delete(DatabaseContract.CityInfo.TABLE_NAME, null, null);
                if (delete > 0) {
                    ContentUris.withAppendedId(uri, delete);
                    Log.v("Sateesh: ", "**** City records deleted " + delete);
                } else {
                    try {
                        throw new SQLException("No City records to Delete" + uri);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        }
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
