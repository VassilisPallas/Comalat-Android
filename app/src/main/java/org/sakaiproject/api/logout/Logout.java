package org.sakaiproject.api.logout;

import android.content.Context;

import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 10/25/15.
 */
public class Logout {
    private Connection connection;

    public Logout(Context context) {
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    public Integer logout(String url) {
        try {
            connection.openConnection(url, ConnectionType.DELETE, false, true, null);
            Integer status = connection.getResponseCode();

            if (status >= 200 && status < 300) {
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return 0;
    }
}
