package org.sakaiproject.api.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sakaiproject.api.general.SystemNotifications;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.logout.Logout;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;
import org.sakaiproject.sakai.MainActivity;

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
            /* get the idle time in milliseconds
               the roundMultiple rounds the milliseconds on the multiplies of 60000
            */
            idle = roundMultiple(System.currentTimeMillis() - lastUsed, 60000);
            Log.d(TAG, "Application is idle for " + idle + " ms");
            count--;

            /* activityIsVisible is true if the app is on foreground
               messageIsVisible is true if the notification has already
               shown to the status bar
            */
            if (idle == (period / 2) && !activityIsVisible && !messageIsVisible) {
                messageIsVisible = true;
                SystemNotifications systemNotifications = new SystemNotifications(context);
                systemNotifications.showSessionNotification();
            } else if (idle == (period / 2) && activityIsVisible) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            /* if idle is equals with the expiration time of the session
               then delete the session and logout the user
            */
            if (idle == period) {
                idle = 0;
                Log.i("end", "end");

                Logout logout = new Logout();

                /* if app is on the foreground after the logout it will start MainActivity
                   else it will only logout the user
                */
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
                    messageTextView.setText("Your session will timeout in " + finalCount + " minutes");
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


    /**
     * the roundMultiple rounds the milliseconds on the multiplies of the selected number
     * eg.
     * 50000 -> 60000
     * 60002 -> 60000
     * 130000 -> 120000
     *
     * @param num      the number that must make round and convert it to the closest multiple of the number that we selected
     * @param multiple the selected number for making the multiple convertion
     * @return the closest multiple
     */
    public long roundMultiple(long num, int multiple) {

        if (num % multiple == 0) {
            return num;
        } else if (num % multiple < (multiple / 2)) {
            num = num - num % multiple;
        } else {
            num = num + (multiple - num % multiple);
        }
        return num;
    }

}
