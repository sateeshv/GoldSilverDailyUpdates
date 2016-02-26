package sateesh.com.goldsilverdailyupdates.Network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import sateesh.com.goldsilverdailyupdates.R;


/**
 * Created by Sateesh on 01-02-2016.
 */
public class ExitWithInternet extends DialogFragment {

    public String neutral_option = "SHARE";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.v("Sateesh: ", "*** No Internet Dialog Exit");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Exit");
        builder.setMessage("Would you like to Exit the app?");
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.v("Sateesh: ", "*** No Internet Exit Dialog Dismissed");
            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                getActivity().onBackPressed();
                System.exit(0);
                Log.v("Sateesh: ", "*** Exit app dialog exited");
            }
        });

        builder.setNeutralButton(neutral_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (neutral_option){
                    case "SHARE":
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case "RATE US":
                        Uri uri = Uri.parse("market://details?id=" +   getActivity().getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(myAppLinkToMarket);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });

        Dialog dialog = builder.create();


        return dialog;
    }
}
