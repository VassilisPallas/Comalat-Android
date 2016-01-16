package org.sakaiproject.api.signup;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by vasilis on 10/17/15.
 * Create new user to sakai
 */
public class Signup {

    private InputStream inputStream;
    private Context context;
    String userId;
    private Connection connection;

    public Signup(Context context) {
        this.context = context;
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    public boolean signUp(String url, String eid, String firstName, String lastName, String email, String password) {

        String data = null;
        try {
            data = "eid=" + URLEncoder.encode(eid, "UTF-8");

            if (firstName != null && !firstName.isEmpty())
                data += "&firstName=" + URLEncoder.encode(firstName, "UTF-8");

            if (lastName != null && !lastName.isEmpty())
                data += "&lastName=" + URLEncoder.encode(lastName, "UTF-8");

            if (email != null && !email.isEmpty())
                data += "&email=" + URLEncoder.encode(email, "UTF-8");

            data += "&password=" + URLEncoder.encode(password, "UTF-8");

            data += "&type=" + URLEncoder.encode("registered", "UTF-8");

            connection.openConnection(url, ConnectionType.POST, false, false, data);

            Integer status = connection.getResponseCode();
            Log.i("status", String.valueOf(status));
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                userId = Actions.readJsonStream(inputStream);
                inputStream.close();
                Log.i("userId", userId);

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return false;
    }

    public boolean eidExists(String eid) {
        try {
            String url = context.getResources().getString(R.string.url) + "user/" + eid + "/exists.json";
            connection.openConnection(url, ConnectionType.GET, false, false, null);
            Integer status = connection.getResponseCode();

            if (status >= 200 && status < 300)
                return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return false;
    }


}
