package org.sakaiproject.api.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.sakaiproject.api.pojos.login.Login;
import org.sakaiproject.api.pojos.login.Profile;
import org.sakaiproject.api.pojos.login.UserData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.workspace.OfflineWorkspace;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.json.JsonParser;

import java.io.File;
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
    private ProgressBar progressBar;
    private TextView loginTextView;
    private LoginType loginType;
    private final Gson gson = new Gson();

    /**
     * the OfflineLogin constructor
     *
     * @param progressBar the progressbar
     * @param context     the context
     */
    public OfflineLogin(ProgressBar progressBar, TextView loginTextView, Context context) {
        this.progressBar = progressBar;
        this.loginTextView = loginTextView;
        this.context = context;
        jsonParse = new JsonParser(context);
        passwordEncryption = new PasswordEncryption();
        progressBar.setVisibility(View.VISIBLE);
        loginTextView.setVisibility(View.GONE);
    }


    @Override
    public void login(String... params) {
        SharedPreferences prefs = context.getSharedPreferences(params[0] + "_user_data", context.MODE_PRIVATE);
        try {
            if (prefs.getString("user_id", null) != null && prefs.getString("password", null) != null) {
                if (params[0].equals(prefs.getString("user_id", null)) && passwordEncryption.check(params[1], prefs.getString("password", null))) {
                    getLoginJson(params[0]);
                    getUserDataJson(params[0]);
                    getUserProfileDataJson(params[0]);
                    OfflineWorkspace offlineWorkspace = new OfflineWorkspace(context, User.getUserId());
                    offlineWorkspace.setLogin(true);
                    offlineWorkspace.getWorkspace();

                    return;
                }
            } else {
                loginType = LoginType.FIRST_TIME_LOGIN_WITHOUT_INTERNET;
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginType = LoginType.INVALID_ARGUMENTS;
        progressBar.setVisibility(View.GONE);
        loginTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getLoginJson(String... params) throws IOException {
        if (ActionsHelper.createDirIfNotExists(context, params[0] + File.separator + "user")) {

            loginJson = ActionsHelper.readJsonFile(context, "loginJson", params[0] + File.separator + "user");

            Login login = gson.fromJson(loginJson, Login.class);

            JsonParser.parseLoginResult(login);
        }
    }

    @Override
    public void getUserDataJson(String... params) throws IOException {
        if (ActionsHelper.createDirIfNotExists(context, params[0] + File.separator + "user")) {
            userDataJson = ActionsHelper.readJsonFile(context, "fullUserDataJson", params[0] + File.separator + "user");

            UserData userData = gson.fromJson(userDataJson, UserData.class);

            JsonParser.parseUserDataJson(userData);
        }
    }

    @Override
    public void getUserProfileDataJson(String... params) throws IOException {
        if (ActionsHelper.createDirIfNotExists(context, params[0] + File.separator + "user")) {
            userProfileDataJson = ActionsHelper.readJsonFile(context, "userProfileDataJson", params[0] + File.separator + "user");

            Profile profile = gson.fromJson(userProfileDataJson, Profile.class);

            JsonParser.parseUserProfileDataJson(profile);
        }
    }

    @Override
    public void getUserImage(String... params) throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getUserThumbnailImage(String... params) throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoginType getLoginType() {
        return loginType;
    }

}
