package org.sakaiproject.api.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import org.sakaiproject.api.server.LoginActions;
import org.sakaiproject.api.cryptography.PasswordService;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.data.UserData;
import org.sakaiproject.api.user.data.UserProfileData;
import org.sakaiproject.api.user.data.UserSessionData;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by vasilis on 10/18/15.
 */
public class OfflineLogin implements ILogin {
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

    public OfflineLogin(Context context) {
        this.context = context;
        userData = new UserData();
        userSessionData = new UserSessionData();
        userProfileData = new UserProfileData();
        jsonParse = new JsonParser();
        passwordService = new PasswordService();
    }

    @Override
    public UserSessionData getUserSessionData() {
        return userSessionData;
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public UserProfileData getUserProfileData() {
        return userProfileData;
    }

    @Override
    public Bitmap getImage() {
        return userImage;
    }

    @Override
    public Bitmap getThumbnailImage() {
        return userThumbnailImage;
    }

    @Override
    public LoginType login(String... params) {
        SharedPreferences prefs = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        try {
            if (prefs.getString("user_id", null) != null && prefs.getString("password", null) != null) {
                if (prefs.getString("user_id", null).equals(params[0]) && passwordService.check(params[1], prefs.getString("password", null))) {
                    getLoginJson();
                    getUserDataJson();
                    getUserProfileDataJson();
                    userImage = getUserImage();
                    userThumbnailImage = getUserThumbnailImage();
                    return LoginType.LOGIN_WITHOUT_INTERNET;
                }
            } else
                return LoginType.FIRST_TIME_LOGIN_WITHOUT_INTERNET;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LoginType.INVALID_ARGUMENTS;
    }

    @Override
    public void getLoginJson(String... params) throws IOException {
        loginJson = LoginActions.readJsonFile(context, "loginJson");
        userSessionData = jsonParse.parseLoginResult(loginJson);
    }

    @Override
    public void getUserDataJson(String... params) throws IOException {
        userDataJson = LoginActions.readJsonFile(context, "fullUserDataJson");
        userData = jsonParse.parseUserDataJson(userDataJson);
    }

    @Override
    public void getUserProfileDataJson(String... params) throws IOException {
        userProfileDataJson = LoginActions.readJsonFile(context, "userProfileDataJson");
        userProfileData = jsonParse.parseUserProfileDataJson(userProfileDataJson);
    }

    @Override
    public Bitmap getUserImage(String... params) throws FileNotFoundException {
        return LoginActions.getImage(context, "user_image");
    }

    @Override
    public Bitmap getUserThumbnailImage(String... params) throws FileNotFoundException {
        return LoginActions.getImage(context, "user_thumbnail_image");
    }

}
