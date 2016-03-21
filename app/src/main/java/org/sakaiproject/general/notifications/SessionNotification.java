package org.sakaiproject.general.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 10/31/15.
 */
public class SessionNotification {

    private Context context;
    private Class<?> activity;
    private static NotificationManager notificationManager;

    /**
     * SystemNotifications constructor
     *
     * @param context  the context
     * @param activity the Class<?> type of activity (MyActivity.class)
     */
    public SessionNotification(Context context, Class<?> activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * notification for session expiration
     * when the app is in idle mode for 15 minuted, this
     * notification warns the user that in 15 minutes the session
     * will expire.
     */
    public void showSessionNotification() {
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // intent triggered
        Intent intent = new Intent(context, activity);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.session_expiration))
                .setContentText(context.getResources().getString(R.string.session_expiration_on_fifteen_minutes))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .build();

        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // hide the notification after it was selected
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }

    public static void cancel(int index) {
        if (notificationManager != null)
            notificationManager.cancel(index);
    }

}
