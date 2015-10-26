package org.sakaiproject.api.logout;

import android.util.Log;

import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 10/25/15.
 */
public class Logout {
    private Connection connection;

    public Logout() {
        connection = Connection.getInstance();
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
        }
        return 0;
    }
}
