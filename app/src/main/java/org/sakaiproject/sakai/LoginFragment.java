package org.sakaiproject.sakai;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sakaiproject.api.login.LoginType;
import org.sakaiproject.api.cryptography.PasswordService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.login.OfflineLogin;
import org.sakaiproject.api.login.OnlineLogin;
import org.sakaiproject.api.user.data.UserData;
import org.sakaiproject.api.user.data.UserProfileData;
import org.sakaiproject.api.user.data.UserSessionData;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private String url;
    private ProgressBar progressBar;

    private OnlineLogin onlineOnlineLogin;
    private OfflineLogin offlineOfflineLogin;
    private UserData userData;
    private UserSessionData userSessionData;
    private UserProfileData userProfileData;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        findViewsById(v);
        return v;
    }

    private void findViewsById(View v) {
        usernameEditText = (EditText) v.findViewById(R.id.username);
        passwordEditText = (EditText) v.findViewById(R.id.password);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    public void clearEditTexts() {
        usernameEditText.setText("");
        passwordEditText.setText("");
        usernameEditText.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                url = getResources().getString(R.string.url) + "session";
                onlineOnlineLogin = new OnlineLogin(getContext());
                offlineOfflineLogin = new OfflineLogin(getContext());
                new LoginAsync(url, usernameEditText.getText().toString(), passwordEditText.getText().toString()).execute();
                break;
        }
    }

    public class LoginAsync extends AsyncTask<Void, Void, LoginType> {

        private String username, password, url;

        LoginAsync(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected LoginType doInBackground(Void... params) {

            if (NetWork.getConnectionEstablished()) {
                return onlineOnlineLogin.login(url, username, password);
            }
            return offlineOfflineLogin.login(username, password);
        }

        @Override
        protected void onPostExecute(LoginType type) {
            super.onPostExecute(type);
            progressBar.setVisibility(View.GONE);

            Bitmap userImage = null;
            Bitmap userThumbnailImage = null;

            if (type == LoginType.LOGIN_WITH_INTERNET) /* connection completed with internet */ {

                PasswordService passwordService = new PasswordService();

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_data", getActivity().MODE_PRIVATE).edit();
                editor.putString("user_id", username);
                editor.putString("password", passwordService.encrypt(password));
                editor.commit();

                userData = onlineOnlineLogin.getUserData();
                userSessionData = onlineOnlineLogin.getUserSessionData();
                userProfileData = onlineOnlineLogin.getUserProfileData();
                userImage = onlineOnlineLogin.getImage();
                userThumbnailImage = onlineOnlineLogin.getThumbnailImage();

            } else if (type == LoginType.LOGIN_WITHOUT_INTERNET) /* connection completed without internet */ {

                userData = offlineOfflineLogin.getUserData();
                userSessionData = offlineOfflineLogin.getUserSessionData();
                userProfileData = offlineOfflineLogin.getUserProfileData();

                userImage = offlineOfflineLogin.getImage();
                userThumbnailImage = offlineOfflineLogin.getThumbnailImage();


            } else if (type == LoginType.FIRST_TIME_LOGIN_WITHOUT_INTERNET) /* if the user tries for the very first time to login without internet connection */ {
                Toast.makeText(getContext(), "You have never login again!\nTo login without internet\nyou have to access at least\none time with internet connection", Toast.LENGTH_LONG).show();
                clearEditTexts();
                return;
            } else /* connection failed */ {
                Toast.makeText(getContext(), "Invalid login", Toast.LENGTH_SHORT).show();
                clearEditTexts();
                return;
            }

            clearEditTexts();
            Intent i = new Intent(getContext(), UserActivity.class);
            i.putExtra("user_data", userProfileData);
            i.putExtra("user_image", userImage);
            i.putExtra("user_thumbnail_image", userThumbnailImage);
            startActivity(i);
            getActivity().finish();

        }
    }
}
