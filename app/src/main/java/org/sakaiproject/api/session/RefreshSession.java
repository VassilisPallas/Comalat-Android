package org.sakaiproject.api.session;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 10/28/15.
 */
public class RefreshSession {

    private Connection connection;
    private Context context;

    public RefreshSession(Context context) {
        this.context = context;
        connection = Connection.getInstance();
    }

    public void putSession(String url) throws IOException {

        String loginJson = Actions.readJsonFile(context, "loginJson");

        connection.openConnection(url, ConnectionType.PUT, false, true, loginJson);
        Integer status = connection.getResponseCode();

        if (status >= 200 && status < 300)
            Log.i("session", "stored");
        else
            Log.i("session", "didn't stored");
    }

}
