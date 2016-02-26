package org.sakaiproject.api.user.profile.update;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.json.JsonWriter;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 1/13/16.
 * update the user info (NOT WORKING BECAUSE THERE IS NO PUT METHOD FOR USER INFO UPDATE)
 */
@Deprecated
public class UpdateUserInfo {
    private Connection connection;
    private JsonWriter jsonWriter;

    /**
     * UpdateUserInfo constructor
     *
     * @param context the context
     */
    public UpdateUserInfo(Context context) {
        connection = Connection.getInstance();
        jsonWriter = new JsonWriter(context);
    }

    /**
     * PUT call to update the user info
     *
     * @param url update the user info (NOT WORKING BECAUSE THERE IS NO PUT METHOD FOR USER INFO UPDATE)
     */
    @Deprecated
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
