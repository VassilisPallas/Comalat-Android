package org.sakaiproject.api.user.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.login.LoginType;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.sakai.AppController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 27/02/16.
 */
public class CheckPassword {

    private OldPasswordMatch callback;
    private String pass;
    private Context context;

    public CheckPassword(OldPasswordMatch callback, String pass, Context context) {
        this.callback = callback;
        this.pass = pass;
        this.context = context;
    }

    public void check() {
        SharedPreferences prefs = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        PasswordEncryption passwordEncryption = new PasswordEncryption();

        if (prefs.getString("password", null) != null) {
            if (passwordEncryption.check(pass, prefs.getString("password", null))) {
                callback.update(true);
            } else {
                callback.update(false);
            }
        }
    }
}
