package nl.ralphrouwen.locationawareapp.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Optional;

import nl.ralphrouwen.locationawareapp.Activitys.DetailedParked_Activity;
import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

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
            //Log.i("VERSION TO LOW", "Make sure your version is above Android 26!");
        }
    }

    //Create the notification that’ll be posted to Channel One//
    public Notification.Builder getNotification1(String title, String body, Optional<Parked> parked) {
        Intent resultIntent;
        // 2 verschillende notificaties:
        //bij 1: homeactivity
        //bij 2: detailed activitiy met het object
        if (parked.isPresent() && parked != null) {
            resultIntent = new Intent(this, DetailedParked_Activity.class);
            resultIntent.putExtra(PARKED_URL, (Parcelable) parked.get());


/*            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            //put backstack intent:
            PendingIntent backToMain = android.app.TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(mainActivityIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            context.getApplicationContext().startActivity(intent);*/


        } else {
            resultIntent = new Intent(this, MainActivity.class);
        }
        //Create intent you want to start-up when clicked on the notification:
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.i("YES", "YESSS");
            return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.appicon2)
                    .setStyle(new Notification.BigTextStyle().bigText(body))
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);
        }

        //Log.i("VERSION TO LOW", "Make sure your version is above Android 26!");
        return null;
    }

    public void postNotification(int id, String title, String body, Optional<Parked> parked) {
        Notification.Builder notificationBuilder = null;

        if (parked.isPresent() && parked != null) { //actual navigation notification
            notificationBuilder = getNotification1(title, body, Optional.ofNullable(parked.get()));
        } else { //reminder
            notificationBuilder = getNotification1(title, body, Optional.empty());
        }

        if (notificationBuilder != null) {
            notify(id, notificationBuilder);
        }
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
