package co.edu.udea.cmovil.gr06.yamba;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID=42;


    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        long[] pattern = new long[]{1000,500,1000};
        int count=intent.getIntExtra("count", 0);

        PendingIntent operation= PendingIntent.getActivity(context, -1, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder= new Notification.Builder(context);
        builder.setContentTitle("Nuevos tweets!");
        if(count==1) {
            builder.setContentText("Tienes " + count + " nuevo tweet");
        }
        else {
            builder.setContentText("Tienes " + count + " nuevos tweets");
        }
        builder.setSmallIcon(android.R.drawable.sym_action_email);
        builder.setContentIntent(operation);
        builder.setAutoCancel(true);
        builder.setVibrate(pattern);
        Notification notification;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
            notification=builder.build();
        }
        else {
            notification = builder.getNotification();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
