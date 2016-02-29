package org.sakaiproject.sakai;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.signup.EidExistence;
import org.sakaiproject.api.signup.SignupService;
import org.sakaiproject.helpers.ActionsHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment implements EidExistence {

    private EidExistence existence = this;
    private EditText eidEditText, firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, typeEditText;
    private TextInputLayout emailTextInputLayout, confirmPasswordTextInputLayout, passwordTextInputLayout, idInputLayout;
    private RelativeLayout signupButton;
    private MaterialRippleLayout rippleLayout;
    private ProgressBar signupProgressBar;
    private TextView signupTextView;

    private boolean isEidValid, isEmailValid = true, isPasswordEqual;

    private FrameLayout root;

    private SignupService signup;

    public SignupFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.new_account));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        findViewsById(v);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!eidEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {
                    String eid = eidEditText.getText().toString().trim();
                    String firstName = firstNameEditText.getText().toString().trim();
                    String lastName = lastNameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    signupProgressBar.setVisibility(View.VISIBLE);

                    signup.signUp(getContext().getResources().getString(R.string.url), eid, firstName, lastName, email, password);
                }
            }
        });

        return v;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void findViewsById(View v) {
        eidEditText = (EditText) v.findViewById(R.id.user_id_edittext);
        firstNameEditText = (EditText) v.findViewById(R.id.first_name_edittext);
        lastNameEditText = (EditText) v.findViewById(R.id.last_name_edittext);
        emailEditText = (EditText) v.findViewById(R.id.email_edittext);
        passwordEditText = (EditText) v.findViewById(R.id.password_edittext);
        confirmPasswordEditText = (EditText) v.findViewById(R.id.confirm_password_edittext);
        typeEditText = (EditText) v.findViewById(R.id.type_edittext);
        signupButton = (RelativeLayout) v.findViewById(R.id.signup_button);
        emailTextInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout_email);
        confirmPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout_confirm_password);
        signupTextView = (TextView) v.findViewById(R.id.sign_up_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            signupTextView.setTextColor(getContext().getResources().getColor(R.color.sign_up_text_gray, getContext().getTheme()));
        } else {
            signupTextView.setTextColor(getContext().getResources().getColor(R.color.sign_up_text_gray));
        }


        passwordTextInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout_password);
        idInputLayout = (TextInputLayout) v.findViewById(R.id.input_layout_user_id);
        rippleLayout = (MaterialRippleLayout) v.findViewById(R.id.ripple);
        signupProgressBar = (ProgressBar) v.findViewById(R.id.signup_progess);

        if (signupProgressBar != null) {
            signupProgressBar.setIndeterminate(true);
            signupProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }

        root = (FrameLayout) v.findViewById(R.id.root);

        if (NetWork.getConnectionEstablished()) {
            eidEditText.addTextChangedListener(new Watcher(eidEditText));
            emailEditText.addTextChangedListener(new Watcher(emailEditText));
            confirmPasswordEditText.addTextChangedListener(new Watcher(confirmPasswordEditText));
            passwordEditText.addTextChangedListener(new Watcher(passwordEditText));
        } else {
            Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getText(R.string.can_not_sync), null).show();
        }

        signup = new SignupService(getContext(), signupProgressBar, signupTextView, idInputLayout);
    }

    private void userEidValidation() {
        String eid = eidEditText.getText().toString().trim();

        signup.eidExists(getContext().getResources().getString(R.string.url) + "user/" + eid + "/exists.json", existence);
    }

    private void emailValidation() {
        String email = emailEditText.getText().toString();
        if (!TextUtils.isEmpty(email))
            if (!isValidEmail(email)) {
                emailTextInputLayout.setError(getString(R.string.no_valid_email_address));
                requestFocus(emailEditText);
                isEmailValid = false;
                return;
            }
        emailTextInputLayout.setErrorEnabled(false);
        isEmailValid = true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void confirmPasswordValidation() {
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!confirmPassword.equals(passwordEditText.getText().toString())) {
            confirmPasswordTextInputLayout.setError(getString(R.string.password_not_match));
            passwordTextInputLayout.setError(getString(R.string.password_not_match));
            requestFocus(confirmPasswordEditText);
            isPasswordEqual = false;
        } else {
            confirmPasswordTextInputLayout.setErrorEnabled(false);
            passwordTextInputLayout.setErrorEnabled(false);
            isPasswordEqual = true;
        }
    }

    private void passwordValidation() {
        String password = passwordEditText.getText().toString();
        if (!password.equals(confirmPasswordEditText.getText().toString())) {
            confirmPasswordTextInputLayout.setError(getString(R.string.password_not_match));
            passwordTextInputLayout.setError(getString(R.string.password_not_match));
            requestFocus(passwordEditText);
            isPasswordEqual = false;
        } else {
            confirmPasswordTextInputLayout.setErrorEnabled(false);
            passwordTextInputLayout.setErrorEnabled(false);
            isPasswordEqual = true;
        }
    }

    public void validation() {
        if (eidEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0
                && isEidValid && isEmailValid && isPasswordEqual) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signupButton.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_normal, getContext().getTheme()));
            } else {
                signupButton.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_normal));
            }
            signupTextView.setTextColor(Color.WHITE);
            signupButton.setClickable(true);
            rippleLayout.setClickable(true);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signupButton.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_disabled, getContext().getTheme()));
            } else {
                signupButton.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_disabled));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signupTextView.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_disabled, getContext().getTheme()));
            } else {
                signupTextView.setBackground(getContext().getResources().getDrawable(R.drawable.layout_button_disabled));
            }

            signupButton.setClickable(false);
            rippleLayout.setClickable(false);
        }
    }

    @Override
    public void signUpButton(boolean exists) {
        isEidValid = exists;
        validation();
    }

    private class Watcher implements TextWatcher {

        View v;

        public Watcher(View v) {
            this.v = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (v.getId()) {
                case R.id.user_id_edittext:
                    userEidValidation();
                    break;
                case R.id.email_edittext:
                    emailValidation();
                    break;
                case R.id.confirm_password_edittext:
                    confirmPasswordValidation();
                    break;
                case R.id.password_edittext:
                    passwordValidation();
                    break;
            }
            validation();
        }
    }
}
