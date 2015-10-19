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

import org.sakaiproject.api.login.LoginAsync;
import org.sakaiproject.api.login.LoginType;
import org.sakaiproject.api.cryptography.PasswordEncryption;
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
        usernameEditText = (EditText) v.findViewById(R.id.loginUsername_EditText);
        passwordEditText = (EditText) v.findViewById(R.id.loginPassword_EditText);
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
                if (!usernameEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals(""))
                    new LoginAsync(this, url, usernameEditText.getText().toString(), passwordEditText.getText().toString()).execute();
                else
                    Toast.makeText(getContext(), "Empty fields", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
