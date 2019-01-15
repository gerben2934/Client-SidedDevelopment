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

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.ACTIVITY_EXTRA;
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
        Intent existingMainActivityIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Final notification
            if (parked.isPresent() && parked != null) {

                //De bestaande intent uit je geschiedenis; Hierop verderbouwen!
                existingMainActivityIntent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());

                existingMainActivityIntent.setPackage(null); // The golden row !!!
                //existingMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                existingMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                existingMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

                resultIntent = new Intent(this, DetailedParked_Activity.class);
                resultIntent.putExtra(ACTIVITY_EXTRA, DetailedParked_Activity.class.getName());
                resultIntent.putExtra(PARKED_URL, (Parcelable) parked.get());

                Log.e("FINAL Notification", "Optie 1");

                PendingIntent pendingIntent = PendingIntent.getActivities(this, 10, new Intent[] { existingMainActivityIntent, resultIntent }, PendingIntent.FLAG_ONE_SHOT);

                return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.appicon2)
                        .setStyle(new Notification.BigTextStyle().bigText(body))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

            } else {
                resultIntent = new Intent(this, MainActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                Log.e("REMINDER Notification", "Optie 2");

                Intent mainScreenIntent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
                mainScreenIntent.setPackage(null); // The golden row !!!
                mainScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntent(mainScreenIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.appicon2)
                        .setStyle(new Notification.BigTextStyle().bigText(body))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            }
        }

        Log.i("VERSION TO LOW", "Make sure your version is above Android 26!");
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
