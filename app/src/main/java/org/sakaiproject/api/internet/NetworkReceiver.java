package org.sakaiproject.api.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.sakaiproject.sakai.MainActivity;

/**
 * Created by vasilis on 10/18/15.
 * a broadcast for checking the internet connection
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetWork.isConnected(context)) {
            Log.i("con", "true");
        } else {
            Log.i("con", "false");
        }
        intent = new Intent(context, MainActivity.class);
        context.startService(intent);
    }

}
