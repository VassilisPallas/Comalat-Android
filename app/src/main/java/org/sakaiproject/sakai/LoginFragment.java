package org.sakaiproject.sakai;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.login.ILogin;
import org.sakaiproject.api.login.LoginService;
import org.sakaiproject.api.login.LoginType;
import org.sakaiproject.api.login.OfflineLogin;
import org.sakaiproject.general.Actions;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText;
    private RelativeLayout loginButton;
    private TextView loginTextView;
    private ProgressBar progressBar;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle(getResources().getString(R.string.login));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        findViewsById(v);

        return v;
    }

    private void findViewsById(View v) {
        usernameEditText = (EditText) v.findViewById(R.id.loginUsername_EditText);
        passwordEditText = (EditText) v.findViewById(R.id.loginPassword_EditText);
        loginButton = (RelativeLayout) v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        progressBar = (ProgressBar) v.findViewById(R.id.loginProgressBar);
        loginTextView = (TextView) v.findViewById(R.id.login_text);

        if (progressBar != null) {
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
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

                if (!usernameEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {

                    String userId = usernameEditText.getText().toString().trim();
                    String pass = passwordEditText.getText().toString().trim();

                    ILogin login;
                    LoginType type;

                    if (NetWork.getConnectionEstablished()) {
                        login = new LoginService(progressBar, loginTextView, getContext());
                        login.login(getResources().getString(R.string.url), userId, pass);

                        clearEditTexts();
                    } else {
                        login = new OfflineLogin(progressBar, loginTextView, getContext());
                        login.login(userId, pass);

                        type = login.getLoginType();

                        if (type == LoginType.FIRST_TIME_LOGIN_WITHOUT_INTERNET) /* if the user tries to login without internet connection for the first time */ {
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.first_login_without_internet), Toast.LENGTH_LONG).show();
                            clearEditTexts();
                            return;
                        } else if (type == LoginType.INVALID_ARGUMENTS) /* connection failed */ {
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
                            clearEditTexts();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        clearEditTexts();
                        Intent i = new Intent(getContext(), UserActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(i);
                        getActivity().finish();
                    }


                } else
                    Toast.makeText(getContext(), getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
