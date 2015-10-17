package org.sakaiproject.api.online.user.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.online.connection.ConnectionParams;
import org.sakaiproject.api.online.user.data.UserData;
import org.sakaiproject.api.online.user.data.UserProfileData;
import org.sakaiproject.api.online.user.data.UserSessionData;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 10/12/15.
 */
public class Login {

    private UserSessionData userSessionData;
    private UserData userData;
    private UserProfileData userProfileData;
    private JsonParser jsonParse;

    private Bitmap bitmap;
    private InputStream inputStream;
    private String sessionId;
    private String loginJson;
    private String fullUserDataJson;
    private Context context;
    private ConnectionParams connection;

    public Login(Context context) {
        this.context = context;
        userData = new UserData();
        userSessionData = new UserSessionData();
        userProfileData = new UserProfileData();
        jsonParse = new JsonParser();
        connection = new ConnectionParams();
    }

    public UserSessionData getUserSessionData() {
        return userSessionData;
    }

    public UserData getUserData() {
        return userData;
    }

    public UserProfileData getUserProfileData() {
        return userProfileData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Integer loginConnection(String... params) {
        try {
            String data = "_username=" + URLEncoder.encode(params[1], "UTF-8");
            data += "&";
            data += "_password=" + URLEncoder.encode(params[2], "UTF-8");

            connection.openConnection(params[0], "POST", false, data);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                sessionId = connection.readStream(inputStream);
                inputStream.close();
                getLoginJson(context.getResources().getString(R.string.url) + "session/" + sessionId + ".json");
                getUserDataJson(context.getResources().getString(R.string.url) + "user/" + userSessionData.getUserEid() + ".json");
                getUserProfileDataJson(context.getResources().getString(R.string.url) + "profile/" + userSessionData.getUserEid() + ".json");
                bitmap = getUserImage(userProfileData.getImageUrl());
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }

        return 0;
    }

    private void getLoginJson(String jsonUrl) {
        try {
            connection.openConnection(jsonUrl, "GET", true, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                loginJson = connection.readStream(inputStream);
                inputStream.close();
                userSessionData = jsonParse.parseLoginResult(loginJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    private void getUserDataJson(String jsonUrl) {
        try {
            connection.openConnection(jsonUrl, "GET", true, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                fullUserDataJson = connection.readStream(inputStream);
                inputStream.close();
                userData = jsonParse.parseUserDataJson(fullUserDataJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    private void getUserProfileDataJson(String jsonUrl) {
        try {
            connection.openConnection(jsonUrl, "GET", true, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                fullUserDataJson = connection.readStream(inputStream);
                inputStream.close();
                userProfileData = jsonParse.parseUserProfileDataJson(fullUserDataJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    private Bitmap getUserImage(String url) {
        Bitmap bitmap = null;
        try {
            connection.openConnection(url, "GET", false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return bitmap;
    }

}
