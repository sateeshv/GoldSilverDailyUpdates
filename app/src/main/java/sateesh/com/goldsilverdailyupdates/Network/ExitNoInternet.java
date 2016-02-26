package sateesh.com.goldsilverdailyupdates.Network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import sateesh.com.goldsilverdailyupdates.R;


/**
 * Created by Sateesh on 01-02-2016.
 */
public class ExitNoInternet extends DialogFragment {

    public String neutral_option = "RATEUS";

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


        Dialog dialog = builder.create();


        return dialog;
    }
}
