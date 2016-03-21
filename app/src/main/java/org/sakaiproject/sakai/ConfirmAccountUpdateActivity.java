package org.sakaiproject.sakai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.update.CheckPassword;
import org.sakaiproject.api.user.update.PasswordMatch;
import org.sakaiproject.api.user.update.UpdateAccountInfoService;

import java.io.IOException;

public class ConfirmAccountUpdateActivity extends AppCompatActivity implements View.OnClickListener, PasswordMatch, Callback {

    private TextView nameTextView, surnameTextView, emailTextView;
    private EditText passEditText;
    private Button save, cancel;
    private ProgressBar progressBar;
    private boolean isPasswordCorrect = false;
    String name, surname, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account_update);

        setTitle(getResources().getString(R.string.confirm_changes));

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        email = getIntent().getStringExtra("email");

        nameTextView = (TextView) findViewById(R.id.first_name_value);
        nameTextView.setText(name);

        surnameTextView = (TextView) findViewById(R.id.last_name_value);
        surnameTextView.setText(surname);

        emailTextView = (TextView) findViewById(R.id.email_value);
        emailTextView.setText(email);

        passEditText = (EditText) findViewById(R.id.user_pass);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        if (progressBar != null) {
            progressBar.setIndeterminate(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent, getTheme()), PorterDuff.Mode.SRC_IN);
            } else {
                progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }
        }

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                goBack(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void update() {
        new CheckPassword(this, passEditText.getText().toString(), getApplicationContext()).check();

        if (isPasswordCorrect) {
            UpdateAccountInfoService service = new UpdateAccountInfoService(
                    getApplicationContext(),
                    name,
                    surname,
                    email,
                    passEditText.getText().toString(),
                    this,
                    progressBar
            );

            try {
                service.update(getResources().getString(R.string.url) + "user/" + User.getUserId() + ".json");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            passEditText.setText("");
            Toast.makeText(this, getResources().getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
        }
    }

    private void goBack(boolean ok) {

        Intent data = new Intent();
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                update();
                break;
            case R.id.cancel:
                goBack(false);
                break;
        }
    }

    @Override
    public void update(boolean match) {
        isPasswordCorrect = match;
    }


    @Override
    public void onSuccess(Object obj) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, getResources().getString(R.string.data_changed), Toast.LENGTH_SHORT).show();

        String fullName = User.getUserEid();
        if (!TextUtils.isEmpty(User.getFirstName())) {
            fullName = User.getFirstName();

            if (!TextUtils.isEmpty(User.getLastName())) {
                fullName += " " + User.getLastName();
            }
        } else if (!TextUtils.isEmpty(User.getLastName())) {
            fullName = User.getLastName();
        }

        UserActivity.displayNameTextView.setText(fullName);
        UserActivity.emailTextView.setText(User.getEmail());
        goBack(true);
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
}
