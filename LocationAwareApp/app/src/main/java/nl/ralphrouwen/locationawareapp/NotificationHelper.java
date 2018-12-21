package nl.ralphrouwen.locationawareapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import android.app.NotificationManager;
import android.graphics.Color;
import android.util.Log;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;

    public static String CHANNEL_ONE_ID;
    public static String CHANNEL_ONE_NAME;

    //Create your notification channels//
    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    // Create the channel object, using the channel ID//
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_ONE_ID = getResources().getString(R.string.notificationChannel_ID);
            CHANNEL_ONE_NAME = getResources().getString(R.string.notificationChannel_name);

            notificationManager = getManager();
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, notificationManager.IMPORTANCE_HIGH);

            //Configure channel initial settings:
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            Log.i("VERSION TO LOW", "Make sure your version is above Android 26!");
        }

    }

    //Create the notification that’ll be posted to Channel One//

    public Notification.Builder getNotification1(String title, String body) {
        Log.i("VERSION", "Version: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("YES", "YESSS");
            return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.appicon1)
                    .setAutoCancel(true);
        }

        Log.i("VERSION TO LOW", "Make sure your version is above Android 26!");
        return null;
    }

    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
}
