package org.sakaiproject.sakai;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.login.LoginAsync;

import java.net.CookieHandler;
import java.net.CookieManager;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private PasswordEncryption passwordEncryption = new PasswordEncryption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.splashImage_progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        NetWork.isConnected(this);

        SharedPreferences prefs = getSharedPreferences("user_logout", MODE_PRIVATE);

        if (!prefs.getBoolean("has_logged_out", true)) {
            new SplashAsync(this).execute();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public class SplashAsync extends AsyncTask<Void, Integer, Void> {

        private Activity a;

        SplashAsync(Activity a) {
            this.a = a;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            SharedPreferences user_prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            String url = getResources().getString(R.string.url) + "session";

            new LoginAsync(a, url, user_prefs.getString("user_id", null), passwordEncryption.decrypt(user_prefs.getString("password", null))).execute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
        }
    }

}
