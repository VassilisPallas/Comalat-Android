package org.sakaiproject.api.site.actions;

import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.IOException;

/**
 * Created by vasilis on 1/22/16.
 */
public class SiteJoin {

    public static boolean join(String url) {
        Connection connection = Connection.getInstance();

        try {
            connection.openConnection(url, ConnectionType.POST, false, false, null);
            Integer status = connection.getResponseCode();

            if (status >= 200 && status < 300) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return false;
    }
}
