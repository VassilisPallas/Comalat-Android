package org.sakaiproject.api.session;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 10/28/15.
 */
public class RefreshSession {

    private Connection connection;
    private Context context;

    /**
     * RefreshSession constructor
     *
     * @param context the context
     */
    public RefreshSession(Context context) {
        this.context = context;
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    public void putSession(String url) {

        try {
            String loginJson = null;
            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "user"))
                loginJson = Actions.readJsonFile(context, "loginJson", User.getUserId() + File.separator + "user");

            connection.openConnection(url, ConnectionType.PUT, false, true, loginJson);
            Integer status = connection.getResponseCode();

            if (status >= 200 && status < 300)
                Log.i("session", "stored");
            else
                Log.i("session", "didn't stored");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }


    }

}
