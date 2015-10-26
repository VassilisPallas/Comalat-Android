package org.sakaiproject.api.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.sakai.LoginFragment;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

/**
 * Created by vasilis on 10/19/15.
 */
public class LoginAsync extends AsyncTask<Void, Void, LoginType> {

    private String username, password, url;

    private OnlineLogin onlineOnlineLogin;
    private OfflineLogin offlineOfflineLogin;


    private Context context;
    private Activity activity;
    private Fragment loginFragment = null;

    private ProgressBar progressBar;
    private EditText usernameEditText, passwordEditText;

    public LoginAsync(Fragment fragment, String url, String username, String password) {
        loginFragment = fragment;
        context = loginFragment.getContext();
        activity = loginFragment.getActivity();
        this.url = url;
        this.username = username;
        this.password = password;


        progressBar = (ProgressBar) activity.findViewById(R.id.loginProgressBar);
        usernameEditText = (EditText) activity.findViewById(R.id.loginUsername_EditText);
        passwordEditText = (EditText) activity.findViewById(R.id.loginPassword_EditText);
    }

    public LoginAsync(Activity activity, String url, String username, String password) {
        this.activity = activity;
        context = activity.getApplication();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private void clearEditTexts() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        usernameEditText.requestFocus();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (loginFragment != null && loginFragment instanceof LoginFragment)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected LoginType doInBackground(Void... params) {

        onlineOnlineLogin = new OnlineLogin(context);
        offlineOfflineLogin = new OfflineLogin(context);

        if (NetWork.getConnectionEstablished()) {
            return onlineOnlineLogin.login(url, username, password);
        }
        return offlineOfflineLogin.login(username, password);
    }

    @Override
    protected void onPostExecute(LoginType type) {
        super.onPostExecute(type);

        if (loginFragment != null && loginFragment instanceof LoginFragment)
            progressBar.setVisibility(View.GONE);

        Bitmap userImage = null;
        Bitmap userThumbnailImage = null;

        if (type == LoginType.LOGIN_WITH_INTERNET) /* connection completed with internet */ {

            PasswordEncryption passwordEncryption = new PasswordEncryption();

            SharedPreferences.Editor editor = context.getSharedPreferences("user_data", context.MODE_PRIVATE).edit();
            editor.putString("user_id", username);
            editor.putString("password", passwordEncryption.encrypt(password));
            editor.commit();

            userImage = onlineOnlineLogin.getImage();
            userThumbnailImage = onlineOnlineLogin.getThumbnailImage();

        } else if (type == LoginType.LOGIN_WITHOUT_INTERNET) /* connection completed without internet */ {

            userImage = offlineOfflineLogin.getImage();
            userThumbnailImage = offlineOfflineLogin.getThumbnailImage();


        } else if (type == LoginType.FIRST_TIME_LOGIN_WITHOUT_INTERNET) /* if the user tries for the very first time to login without internet connection */ {
            Toast.makeText(context, "You have never login again!\nTo login without internet\nyou have to access at least\none time with internet connection", Toast.LENGTH_LONG).show();
            if (loginFragment != null && loginFragment instanceof LoginFragment)
                clearEditTexts();
            return;
        } else /* connection failed */ {
            Toast.makeText(context, "Invalid login", Toast.LENGTH_SHORT).show();
            if (loginFragment != null && loginFragment instanceof LoginFragment)
                clearEditTexts();
            return;
        }


        if (loginFragment != null && loginFragment instanceof LoginFragment)
            clearEditTexts();

        SharedPreferences.Editor editor = context.getSharedPreferences("user_data", context.MODE_PRIVATE).edit();
        editor.putBoolean("has_logged_out", false);
        editor.commit();

        Intent i = new Intent(context, UserActivity.class);
        i.putExtra("user_image", userImage);
        i.putExtra("user_thumbnail_image", userThumbnailImage);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        activity.finish();

    }


}