package com.example.usuario.gcmgrupo6;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATIO_ID=1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String TAG="pavan";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.d(TAG, "in gcm intent message" + messageType);
        Log.d(TAG, "in gsm intent message bundle" + extras);

        if (!extras.isEmpty()) {


            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String received_message = intent.getStringExtra("text_message");
                sendNotification("message recieved: " + received_message);

                Intent sendIntent = new Intent("message recieved");
                sendIntent.putExtra("message", received_message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void sendNotification(String msg){
        mNotificationManager=(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent= PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),0);
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this)
        .setContentTitle("GCM Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATIO_ID, mBuilder.build());
    }
}


