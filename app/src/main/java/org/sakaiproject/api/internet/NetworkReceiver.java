package org.sakaiproject.api.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.sakaiproject.sakai.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 10/18/15.
 * a broadcast for checking the internet connection
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static List<Boolean> connectedList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetWork.isConnected(context)) {

            if (connectedList.size() > 0 && !connectedList.get(connectedList.size() - 1)) {
                //TODO: sync everything
            }
            connectedList.add(true);
            Log.i("con", "true");
        } else {
            connectedList.add(false);
            Log.i("con", "false");
        }
        intent = new Intent(context, MainActivity.class);
        context.startService(intent);
    }

}
