package coursera.project.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Peter on 27/11/2014.
 */
public class SelfieAlarmNotificationReceiver extends BroadcastReceiver {

    // Notification ID
    private static final int SELFIE_NOTIFICATION_ID = 1;

    // Notification Text Elements
    private final CharSequence tickerText = "Daily Selfie Time!";
    private final CharSequence contentTitle = "Daily Selfie Reminder";
    private final CharSequence contentText = "Take a Selfie!!";

    // Notification Action Elements
    private Intent selfieNotificationIntent;
    private PendingIntent dailySelfieIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        // The intent to be used when the user clicks on the notification view
        selfieNotificationIntent = new Intent(context, DailySelfieActivity.class);

        // The pending intent that wraps the underlying intent
        dailySelfieIntent = PendingIntent.getActivity(context, 0,
                selfieNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker(tickerText)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true).setContentTitle(contentTitle)
                .setContentText(contentText).setContentIntent(dailySelfieIntent);

        // Get the Notification Manager
        NotificationManager selfieNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the notification to the NotificationManager
        selfieNotificationManager.notify(SELFIE_NOTIFICATION_ID,
                notificationBuilder.build());

        // Log the occurrence of notify() call
        Log.i("SELFIE", "Sending Selfie notification at: "
                + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
