package org.sakaiproject.api.offline.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.sakaiproject.api.Actions;
import org.sakaiproject.api.cryptography.PasswordService;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.online.connection.ConnectionParams;
import org.sakaiproject.api.user.data.UserData;
import org.sakaiproject.api.user.data.UserProfileData;
import org.sakaiproject.api.user.data.UserSessionData;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vasilis on 10/18/15.
 */
public class Login {
    private UserSessionData userSessionData;
    private UserData userData;
    private UserProfileData userProfileData;
    private JsonParser jsonParse;
    private PasswordService passwordService;

    private Bitmap userImage, userThumbnailImage;
    private String loginJson;
    private String userDataJson;
    private String userProfileDataJson;
    private Context context;

    public Login(Context context) {
        this.context = context;
        userData = new UserData();
        userSessionData = new UserSessionData();
        userProfileData = new UserProfileData();
        jsonParse = new JsonParser();
        passwordService = new PasswordService();
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

    public Bitmap getUserImage() {
        return userImage;
    }

    public Bitmap getUserThumbnailImage() {
        return userThumbnailImage;
    }


    public Integer login(String user_id, String password) {
        SharedPreferences prefs = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        try {
            if (prefs.getString("user_id", null) != null && prefs.getString("password", null) != null) {
                if (prefs.getString("user_id", null).equals(user_id) && passwordService.check(password, prefs.getString("password", null))) {
                    getLoginJson();
                    getUserDataJson();
                    getUserProfileDataJson();
                    getImages();
                    return 2;
                }
            } else
                return 3;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void getLoginJson() throws IOException {
        loginJson = Actions.readJsonFile(context, "loginJson");
        userSessionData = jsonParse.parseLoginResult(loginJson);
    }

    public void getUserDataJson() throws IOException {
        userDataJson = Actions.readJsonFile(context, "fullUserDataJson");
        userData = jsonParse.parseUserDataJson(userDataJson);
    }

    public void getUserProfileDataJson() throws IOException {
        userProfileDataJson = Actions.readJsonFile(context, "userProfileDataJson");
        userProfileData = jsonParse.parseUserProfileDataJson(userProfileDataJson);
    }

    public void getImages() throws FileNotFoundException {
        userImage = Actions.getImage(context, "user_image");
        userThumbnailImage = Actions.getImage(context, "user_thumbnail_image");
    }
}
