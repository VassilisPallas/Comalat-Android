package org.sakaiproject.api.user.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.cryptography.PasswordEncryption;
import org.sakaiproject.api.json.JsonWriter;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.general.Connection;
import org.sakaiproject.sakai.AppController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 27/02/16.
 */
public class UpdateAccountInfoService {
    private Context context;
    private String name, surname, email, pass;
    private final String update_tag = User.getUserEid() + " account update";
    private Callback callback;
    private ProgressBar progressBar;

    public UpdateAccountInfoService(Context context, String name, String surname, String email, String pass, Callback callback, ProgressBar progressBar) {
        this.context = context;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pass = pass;
        this.callback = callback;
        this.progressBar = progressBar;
    }

    public void update(String url) throws IOException, JSONException {

        JSONObject jsonObj = new JSONObject(JsonWriter.updateUserAccountJson(context, name, surname, email, pass));

        progressBar.setVisibility(View.VISIBLE);
        EmptyRequest updateRequest = new EmptyRequest(Request.Method.PUT, url, jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User.setFirstName(name);
                User.setLastName(surname);
                User.setEmail(email);
                SharedPreferences prefs = context.getSharedPreferences(User.getUserEid() + "_user_data", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", new PasswordEncryption().encrypt(pass));
                editor.commit();

                callback.onSuccess("accountUpdate");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(updateRequest, update_tag);
    }


}
