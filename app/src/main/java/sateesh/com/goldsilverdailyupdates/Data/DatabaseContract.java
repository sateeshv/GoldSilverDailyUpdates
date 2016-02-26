package sateesh.com.goldsilverdailyupdates.Data;

import android.net.Uri;

public class DatabaseContract {

    public static final String AUTHORITY = "sateesh.com.goldsilverdailyupdates";
    public static final Uri BASIC_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_PRICE_INFO = "PriceInfo";
    public static final String PATH_CITY_INFO = "CityInfo";


    public static class PriceInfo {
        public static final Uri CONTENT_URI = BASIC_URI.buildUpon().appendPath(PATH_PRICE_INFO).build();
        public static final String TABLE_NAME = "PriceInfo";

        public static final String _ID = "_id";
        public static final String COLUMN_MODIFIED_DATETIME = "ModifiedDateTime";
        public static final String COLUMN_DATE = "PriceDate";
        public static final String COLUMN_CITY_NAME = "CityName";
        public static final String COLUMN_GOLD_1_GM = "Gold1Gram";
        public static final String COLUMN_SILVER_1_GM = "Silver1Gram";
        public static final String COLUMN_GOLD_CHANGE = "IsGoldChanged";
        public static final String COLUMN_SILVER_CHANGE = "IsSilverChanged";

    }

    public static class CityInfo {
        public static final Uri CONTENT_URI = BASIC_URI.buildUpon().appendPath(PATH_CITY_INFO).build();
        public static final String TABLE_NAME = "CityInfo";

        public static final String _ID = "_id";
//        public static final String COLUMN_S_NO = "SNo";
        public static final String COLUMN_MODIFIED_DATETIME = "ModifiedDateTime";
        public static final String COLUMN_CITY_NAME = "CityName";
    }
}
