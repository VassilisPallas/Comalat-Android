package org.sakaiproject.api.user.update;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import org.sakaiproject.api.json.JsonWriter;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.sakai.AppController;

import java.io.IOException;

/**
 * Created by vspallas on 27/02/16.
 */
public class UpdateAccountInfoService {
    private Context context;
    private String name, surname, email, pass;
    private final String update_tag = User.getUserEid() + " account update";
    private OnDataChanged callback;

    public UpdateAccountInfoService(Context context, String name, String surname, String email, String pass, OnDataChanged callback) {
        this.context = context;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pass = pass;
        this.callback = callback;
    }

    public void update(String url) throws IOException, JSONException {

        JSONObject jsonObj = new JSONObject(JsonWriter.updateUserAccountJson(context, name, surname, email, pass));

        EmptyRequest updateRequest = new EmptyRequest(Request.Method.PUT, url, jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User.setFirstName(name);
                User.setLastName(surname);
                User.setEmail(email);

                callback.updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(update_tag, error);
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(updateRequest, update_tag);
    }


}
