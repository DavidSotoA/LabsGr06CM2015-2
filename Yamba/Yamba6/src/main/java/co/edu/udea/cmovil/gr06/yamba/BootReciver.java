package co.edu.udea.cmovil.gr06.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReciver extends BroadcastReceiver {
    private static final String  TAG= BootReciver.class.getSimpleName();
    private static final long DEFAULT_INTERVAL= AlarmManager.INTERVAL_FIFTEEN_MINUTES/150;

    public BootReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long interval= DEFAULT_INTERVAL;

        PendingIntent operation= PendingIntent.getService(context, -1,
                new Intent(context, RefreshService.class),PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval,operation);

        Log.d(TAG, "onReceive");


        throw new UnsupportedOperationException("Not yet implemented");
    }
}
