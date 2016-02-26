package sateesh.com.goldsilverdailyupdates.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import sateesh.com.goldsilverdailyupdates.MainActivity;
import sateesh.com.goldsilverdailyupdates.R;


public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setSmallIcon(R.drawable.increase);
            builder.setAutoCancel(true);
            builder.setContentTitle("Gold & Silver Daily Updates");
            builder.setContentText("New Prices available!");
            builder.setSubText("Tap to view documentation about notifications.");

            Intent myIntent = new Intent(context, MainActivity.class);
            PendingIntent intent2 = PendingIntent.getActivity(context, 1,
                    myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(intent2);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}