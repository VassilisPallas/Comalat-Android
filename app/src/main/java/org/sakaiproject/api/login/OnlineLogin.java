package org.sakaiproject.api.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.sakaiproject.general.Actions;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 10/12/15.
 */
public class OnlineLogin implements ILogin {

    private User user;
    private Profile profile;
    private JsonParser jsonParse;
    private InputStream inputStream;
    private String sessionId;
    private String loginJson;
    private String userDataJson;
    private String userProfileDataJson;
    private Context context;
    private Connection connection;

    /**
     * the OnlineLogin constructor
     *
     * @param context the context
     */
    public OnlineLogin(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
        connection = Connection.getInstance();
        connection.setContext(context);
        user = User.getInstance();
        profile = Profile.getInstance();
    }

    @Override
    public LoginType login(String... params) {
        try {
            String data = "_username=" + URLEncoder.encode(params[1], "UTF-8");
            data += "&";
            data += "_password=" + URLEncoder.encode(params[2], "UTF-8");

            connection.openConnection(params[0], ConnectionType.POST, false, false, data);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                sessionId = Actions.readJsonStream(inputStream);
                inputStream.close();
                connection.setSessionId(sessionId);
                getLoginJson(context.getResources().getString(R.string.url) + "session/" + sessionId + ".json");
                getUserDataJson(context.getResources().getString(R.string.url) + "user/" + user.getUserEid() + ".json");
                getUserProfileDataJson(context.getResources().getString(R.string.url) + "profile/" + user.getUserEid() + ".json");
                getUserImage(profile.getImageUrl());
                getUserThumbnailImage(profile.getImageThumbUrl());

                return LoginType.LOGIN_WITH_INTERNET;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        if (connection.isSocketException()) {
            return LoginType.SOCKETEXCEPTION;
        }

        return LoginType.INVALID_ARGUMENTS;
    }

    @Override
    public void getLoginJson(String... params) throws IOException {
        try {
            connection.openConnection(params[0], ConnectionType.GET, true, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                loginJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseLoginResult(loginJson);
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    Actions.writeJsonFile(context, loginJson, "loginJson", User.getUserEid() + File.separator + "user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    @Override
    public void getUserDataJson(String... params) throws IOException {
        try {
            connection.openConnection(params[0], ConnectionType.GET, true, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                userDataJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseUserDataJson(userDataJson);
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    Actions.writeJsonFile(context, userDataJson, "fullUserDataJson", User.getUserEid() + File.separator + "user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    @Override
    public void getUserProfileDataJson(String... params) throws IOException {
        try {
            connection.openConnection(params[0], ConnectionType.GET, true, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                userProfileDataJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseUserProfileDataJson(userProfileDataJson);
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    Actions.writeJsonFile(context, userProfileDataJson, "userProfileDataJson", User.getUserEid() + File.separator + "user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    @Override
    public void getUserImage(String... params) {
        Bitmap bitmap = null;
        try {
            connection.openConnection(params[0], ConnectionType.GET, false, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(inputStream);
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    Actions.saveImage(context, bitmap, "user_image.jpg", User.getUserEid() + File.separator + "user");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

    @Override
    public void getUserThumbnailImage(String... params) {
        Bitmap bitmap = null;
        try {
            connection.openConnection(params[0], ConnectionType.GET, false, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                bitmap = BitmapFactory.decodeStream(inputStream);
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    Actions.saveImage(context, bitmap, "user_thumbnail_image.jpg", User.getUserEid() + File.separator + "user");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }

}
