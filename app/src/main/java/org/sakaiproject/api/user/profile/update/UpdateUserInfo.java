package org.sakaiproject.api.user.profile.update;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.json.JsonWriter;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 1/13/16.
 */
public class UpdateUserInfo {
    private Connection connection;
    private JsonWriter jsonWriter;

    public UpdateUserInfo(Context context) {
        connection = Connection.getInstance();
        jsonWriter = new JsonWriter(context);
    }

    public void updateInfo(String url) {
        try {
            String infoJson = jsonWriter.updateUserProfileInfo();
            connection.openConnection(url, ConnectionType.PUT, false, false, infoJson);
            Integer status = connection.getResponseCode();

            if (status >= 200 && status < 300)
                Log.i("info", "update");
            else {
                Log.i("status", String.valueOf(status));
                Log.i("info", "error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }
}
