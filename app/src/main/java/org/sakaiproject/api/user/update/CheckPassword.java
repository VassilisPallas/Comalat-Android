package org.sakaiproject.api.user.update;

import android.content.Context;
import android.content.SharedPreferences;

import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.user.User;

/**
 * Created by vspallas on 27/02/16.
 */
public class CheckPassword {

    private PasswordMatch callback;
    private String pass;
    private Context context;

    public CheckPassword(PasswordMatch callback, String pass, Context context) {
        this.callback = callback;
        this.pass = pass;
        this.context = context;
    }

    public void check() {
        SharedPreferences prefs = context.getSharedPreferences(User.getUserEid() + "_user_data", context.MODE_PRIVATE);
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
