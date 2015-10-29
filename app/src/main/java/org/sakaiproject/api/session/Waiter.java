package org.sakaiproject.api.session;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.logout.Logout;
import org.sakaiproject.api.user.data.Profile;
import org.sakaiproject.api.user.data.User;
import org.sakaiproject.sakai.MainActivity;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

/**
 * Created by vasilis on 10/28/15.
 * Checks if the app is on idle mode for 30 minutes
 * and shows message to the user about the session
 */
public class Waiter extends Thread {

    private static Waiter instance = null;

    private static final String TAG = Waiter.class.getName();
    private long lastUsed;
    private long period;
    public volatile boolean stop;
    private boolean messageIsVisible = false;
    private boolean activityIsVisible;
    private Context context;
    private static long idle = 0;
    private Activity activity;
    private RelativeLayout messageRelativeLayout;
    private TextView messageTextView;
    private int count = 31;

    private Waiter() {
    }

    public static synchronized Waiter getInstance() {
        if (instance == null)
            instance = new Waiter();
        return instance;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isMessageVisible() {
        return messageIsVisible;
    }

    public void setActivityIsVisible(boolean activityIsVisible) {
        this.activityIsVisible = activityIsVisible;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setMessageRelativeLayout(RelativeLayout messageRelativeLayout) {
        this.messageRelativeLayout = messageRelativeLayout;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public void run() {
        idle = 0;
        this.touch();
        do {
            idle = roundMultiple(System.currentTimeMillis() - lastUsed);
            Log.d(TAG, "Application is idle for " + idle + " ms");
            count--;

            if (idle == (period / 2) && !activityIsVisible && !messageIsVisible) {
                messageIsVisible = true;
                showNotification();
            } else if (idle == (period / 2) && activityIsVisible) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            if (idle == period) {
                idle = 0;
                Log.i("end", "end");

                Logout logout = new Logout();

                if (activityIsVisible) {
                    if (NetWork.getConnectionEstablished()) {
                        try {
                            if (logout.logout("http://141.99.248.86:8089/direct/session/" + Connection.getSessionId()) == 1) {
                                User.nullInstance();
                                Profile.nullInstance();
                                Connection.nullSessionId();

                                Intent i = new Intent(context, MainActivity.class);
                                context.startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        User.nullInstance();
                        Profile.nullInstance();
                        Connection.nullSessionId();

                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                    }
                } else {
                    try {
                        if (logout.logout("http://141.99.248.86:8089/direct/session/" + Connection.getSessionId()) == 1) {
                            User.nullInstance();
                            Profile.nullInstance();
                            Connection.nullSessionId();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                stop = true;
            }


            final int finalCount = count;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageTextView.setText("Your session will expire in " + finalCount + " minutes");
                }
            });

            if (!stop) {
                try {
                    Thread.sleep(60 * 1000); //check every 1 minute
                } catch (InterruptedException e) {
                    Log.d(TAG, "Waiter interrupted!");
                }
            }
        }
        while (!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void touch() {
        lastUsed = System.currentTimeMillis();
    }

    public synchronized void forceInterrupt() {
        this.interrupt();
    }

    public synchronized void setPeriod(long period) {
        this.period = period;
    }

    public void showNotification() {
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered
        Intent intent = new Intent(context, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Session expiration")
                .setContentText("Session will expire in less than 15 minutes")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // hide the notification after it was selected
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);

    }

    public long roundMultiple(long num) {

        if (num % 60000 == 0) {
            return num;
        } else if (num % 60000 < (60000 / 2)) {
            num = num - num % 60000;
        } else {
            num = num + (60000 - num % 60000);
        }
        return num;
    }

}
