package org.sakaiproject.general.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;

/**
 * Created by vspallas on 03/03/16.
 */
public class DownloadNotification {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private Context context;
    int id = 1;
    private File file = null;

    public DownloadNotification(Context context) {
        this.context = context;
    }

    public void setPath(File file) {
        this.file = file;
    }

    public void showNotification() {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.file_download))
                .setContentText(context.getResources().getString(R.string.download_in_progress))
                .setSmallIcon(android.R.drawable.stat_sys_download);
        mBuilder.setProgress(0, 0, true);
        notificationManager.notify(id, mBuilder.build());
    }

    public void hideNotification() {

        mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);

        Intent intent = ActionsHelper.openFile(context, file);
        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.no_intent_app), Toast.LENGTH_SHORT).show();
        }
        // updates the notification
        mBuilder.setContentText(context.getResources().getString(R.string.download_complete))
                .setAutoCancel(true)
                        // Removes the progress bar
                .setProgress(0, 0, false);
        notificationManager.notify(id, mBuilder.build());

    }
}
