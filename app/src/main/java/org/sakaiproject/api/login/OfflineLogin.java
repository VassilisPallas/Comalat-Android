package org.sakaiproject.api.login;

import android.content.Context;
import android.content.SharedPreferences;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by vasilis on 10/18/15.
 */
public class OfflineLogin implements ILogin {

    private JsonParser jsonParse;
    private PasswordEncryption passwordEncryption;
    private String loginJson;
    private String userDataJson;
    private String userProfileDataJson;
    private Context context;

    public OfflineLogin(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
        passwordEncryption = new PasswordEncryption();
    }

    @Override
    public LoginType login(String... params) {
        SharedPreferences prefs = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        try {
            if (prefs.getString("user_id", null) != null && prefs.getString("password", null) != null) {
                if (prefs.getString("user_id", null).equals(params[0]) && passwordEncryption.check(params[1], prefs.getString("password", null))) {
                    getLoginJson();
                    getUserDataJson();
                    getUserProfileDataJson();
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
        loginJson = Actions.readJsonFile(context, "loginJson");
        jsonParse.parseLoginResult(loginJson);
    }

    @Override
    public void getUserDataJson(String... params) throws IOException {
        userDataJson = Actions.readJsonFile(context, "fullUserDataJson");
        jsonParse.parseUserDataJson(userDataJson);
    }

    @Override
    public void getUserProfileDataJson(String... params) throws IOException {
        userProfileDataJson = Actions.readJsonFile(context, "userProfileDataJson");
        jsonParse.parseUserProfileDataJson(userProfileDataJson);
    }

    @Override
    public void getUserImage(String... params) throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getUserThumbnailImage(String... params) throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

}
