package sateesh.com.goldsilverdailyupdates.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sateesh on 30-01-2016.
 */
public class InternetCheck {
    private static Context _context;
//
//    public InternetCheck(Context context) {
//        this._context = context;
//    }

    public static boolean isConnectingToInternet(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else
            return false;
    }
}
