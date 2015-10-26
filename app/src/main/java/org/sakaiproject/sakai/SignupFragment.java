package org.sakaiproject.sakai;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.sakaiproject.api.signup.Signup;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private EditText eidEditText, firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, typeEditText;
    private Button signupButton;
    private Signup signup;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        signup = new Signup(getContext());

        findViewsById(v);

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
                    new SignupAsync(url, eid, firstName, lastName, email, password).execute();
                }
            }
        });

        // TODO: TextWatcher for eidEditText existence and for the equalization of passwordEditText and confirm passwordEditText

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
    }

    private class SignupAsync extends AsyncTask<Void, Void, Boolean> {

        private String url, eid, fname, lname, mail, pass;

        public SignupAsync(String url, String eid, String fname, String lname, String mail, String pass) {
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
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return signup.signUp(url, eid, fname, lname, mail, pass);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //new LoginAsync().execute();
        }
    }

    private class EidExistsAsync extends AsyncTask<Void, Void, Boolean> {

        private String url, eid;

        public EidExistsAsync(String url, String eid) {
            this.url = url;
            this.eid = eid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return signup.eidExists(eid);
        }

        @Override
        protected void onPostExecute(Boolean exists) {
            super.onPostExecute(exists);
        }
    }

}
