package org.sakaiproject.sakai;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.sakaiproject.api.login.LoginAsync;
import org.sakaiproject.api.signup.Signup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private Fragment fragment;

    private EditText eidEditText, firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, typeEditText;
    private Button signupButton;
    private ProgressBar userExistsProgressBar, signupProgressBar;
    private ImageView userExistsImageView, emailValidationImageView, passwordEqualsImageView;
    private Signup signup;

    private String emailRegex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private boolean isEidValid, isEmailValid = true, isPasswordEqual;

    public SignupFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        getActivity().setTitle("New Account");

        signup = new Signup(getContext());

        findViewsById(v);

        fragment = this;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getContext().getResources().getString(R.string.url) + "user";
                if (!eidEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {
                    String eid = eidEditText.getText().toString().trim();
                    String firstName = firstNameEditText.getText().toString().trim();
                    String lastName = lastNameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    new SignupAsync(fragment, url, eid, firstName, lastName, email, password).execute();
                }
            }
        });

        return v;
    }

    private void findViewsById(View v) {
        eidEditText = (EditText) v.findViewById(R.id.user_id_edittext);
        firstNameEditText = (EditText) v.findViewById(R.id.first_name_edittext);
        lastNameEditText = (EditText) v.findViewById(R.id.last_name_edittext);
        emailEditText = (EditText) v.findViewById(R.id.email_edittext);
        passwordEditText = (EditText) v.findViewById(R.id.password_edittext);
        confirmPasswordEditText = (EditText) v.findViewById(R.id.confirm_password_edittext);
        typeEditText = (EditText) v.findViewById(R.id.type_edittext);
        signupButton = (Button) v.findViewById(R.id.signup_button);
        userExistsProgressBar = (ProgressBar) v.findViewById(R.id.user_exists_progressbar);
        userExistsImageView = (ImageView) v.findViewById(R.id.user_exists_imageview);
        emailValidationImageView = (ImageView) v.findViewById(R.id.email_validation_imageview);
        passwordEqualsImageView = (ImageView) v.findViewById(R.id.password_equals_imageview);
        signupProgressBar = (ProgressBar) v.findViewById(R.id.signup_progess);

        eidEditText.addTextChangedListener(userExistWatcher);
        emailEditText.addTextChangedListener(emailValidationWatcher);
        confirmPasswordEditText.addTextChangedListener(confirmPasswordWatcher);
        passwordEditText.addTextChangedListener(passwordChangeWatcher);
    }

    private TextWatcher userExistWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String eid = eidEditText.getText().toString().trim();
            new EidExistsAsync(eid).execute();
        }
    };

    private TextWatcher emailValidationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!s.toString().trim().matches(emailRegex)) {

                isEmailValid = false;

            } else {

                isEmailValid = true;

            }

            if (emailEditText.getText().toString().length() == 0) {
                isEmailValid = true;
                emailValidationImageView.setVisibility(View.GONE);
                validation();
                return;
            }

            emailValidationImageView.setVisibility(View.VISIBLE);
            emailValidationImageView.setImageDrawable(selectValidationImage(isEmailValid));

            validation();
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().trim().matches(emailRegex)) {

                isEmailValid = false;

            } else {

                isEmailValid = true;

            }

            if (emailEditText.getText().toString().length() == 0) {
                isEmailValid = true;
                emailValidationImageView.setVisibility(View.GONE);
                validation();
                return;
            }

            emailValidationImageView.setVisibility(View.VISIBLE);
            emailValidationImageView.setImageDrawable(selectValidationImage(isEmailValid));

            validation();
        }
    };

    private TextWatcher confirmPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!s.toString().equals(passwordEditText.getText().toString()) || count == 0) {
                isPasswordEqual = false;
            } else {
                isPasswordEqual = true;
            }

            passwordEqualsImageView.setVisibility(View.VISIBLE);
            passwordEqualsImageView.setImageDrawable(selectValidationImage(isPasswordEqual));

            validation();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(passwordEditText.getText().toString())) {
                isPasswordEqual = false;
            } else {
                isPasswordEqual = true;
            }

            passwordEqualsImageView.setVisibility(View.VISIBLE);
            passwordEqualsImageView.setImageDrawable(selectValidationImage(isPasswordEqual));

            validation();
        }
    };

    private TextWatcher passwordChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.equals(confirmPasswordEditText.getText().toString())) {
                isPasswordEqual = false;
            } else {
                isPasswordEqual = true;
            }

            passwordEqualsImageView.setVisibility(View.VISIBLE);
            passwordEqualsImageView.setImageDrawable(selectValidationImage(isPasswordEqual));

            validation();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(confirmPasswordEditText.getText().toString())) {
                isPasswordEqual = false;
            } else {
                isPasswordEqual = true;
            }

            passwordEqualsImageView.setVisibility(View.VISIBLE);
            passwordEqualsImageView.setImageDrawable(selectValidationImage(isPasswordEqual));

            validation();
        }
    };

    public void validation() {
        if (eidEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0
                && isEidValid && isEmailValid && isPasswordEqual) {
            signupButton.setEnabled(true);
        } else {
            signupButton.setEnabled(false);
        }
    }

    private Drawable selectValidationImage(boolean correct) {

        try {
            Drawable image;

            int imageId = R.mipmap.ic_cancel;
            int color = Color.RED;

            if (correct) {
                imageId = R.mipmap.ic_check_circle;
                color = Color.parseColor("#03AD14");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image = getResources().getDrawable(imageId, getActivity().getTheme());
            } else {
                image = getResources().getDrawable(imageId);
            }

            image.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            //return ((BitmapDrawable) image).getBitmap();
            return image;
        } catch (IllegalStateException e) {

        }
        return null;
    }

    private class SignupAsync extends AsyncTask<Void, Void, Boolean> {

        private String url, eid, fname, lname, mail, pass;
        private Fragment fragment;

        public SignupAsync(Fragment fragment, String url, String eid, String fname, String lname, String mail, String pass) {
            this.fragment = fragment;
            this.url = url;
            this.eid = eid;
            this.fname = fname;
            this.lname = lname;
            this.mail = mail;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            signupProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return signup.signUp(url, eid, fname, lname, mail, pass);
        }

        @Override
        protected void onPostExecute(Boolean signup) {
            super.onPostExecute(signup);
            if (signup) {
                url = getResources().getString(R.string.url) + "session";
                new LoginAsync(fragment, url, eid, pass).execute();

            }
            signupProgressBar.setVisibility(View.GONE);
        }
    }

    private class EidExistsAsync extends AsyncTask<Void, Void, Boolean> {

        private String eid;

        public EidExistsAsync(String eid) {
            this.eid = eid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userExistsImageView.setVisibility(View.INVISIBLE);
            userExistsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return signup.eidExists(eid);
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            super.onPostExecute(exists);
            userExistsImageView.setVisibility(View.VISIBLE);
            userExistsProgressBar.setVisibility(View.GONE);

            if (eidEditText.getText().toString().length() == 0)
                isEidValid = exists;
            else
                isEidValid = !exists;

            userExistsImageView.setImageDrawable(selectValidationImage(isEidValid));

            validation();
        }
    }

}
