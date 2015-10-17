package org.sakaiproject.sakai;


import android.content.Intent;
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

import org.sakaiproject.api.user.data.UserData;
import org.sakaiproject.api.user.data.UserProfileData;
import org.sakaiproject.api.user.data.UserSessionData;
import org.sakaiproject.api.user.login.Login;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private String url;
    private ProgressBar progressBar;

    private Login login;
    private UserData user;
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
                login = new Login(getContext());
                new LoginAsync(url, usernameEditText.getText().toString(), passwordEditText.getText().toString()).execute();
                break;
        }
    }

    public class LoginAsync extends AsyncTask<Void, Void, Integer> {

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
        protected Integer doInBackground(Void... params) {
            return login.loginConnection(url, username, password);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.GONE);
            if (integer == 1) {
                user = login.getUserData();
                userSessionData = login.getUserSessionData();
                userProfileData = login.getUserProfileData();
                Bitmap userImage = login.getBitmap();
                clearEditTexts();
                Intent i = new Intent(getContext(), UserActivity.class);
                i.putExtra("user_data", userProfileData);
                i.putExtra("user_image", userImage);
                startActivity(i);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Invalid login", Toast.LENGTH_SHORT).show();
                clearEditTexts();
            }
        }
    }
}
