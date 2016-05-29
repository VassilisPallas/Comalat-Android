package org.sakaiproject.api.user.update;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.login.UserData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 28/02/16.
 */
public class AccountDataService {
    private Context context;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;
    private final String tag_user_data_json = User.getUserEid() + " data json";

    public AccountDataService(Context context, CustomSwipeRefreshLayout swipeRefreshLayout, Callback callback) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.callback = callback;
    }

    public void accountInfo(String url) {
        JsonObjectRequest accountInfoRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserData userData = gson.fromJson(response.toString(), UserData.class);

                JsonParser.parseUserDataJson(userData);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "fullUserDataJson", User.getUserEid() + File.separator + "user");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                callback.onSuccess("updateData");
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

        AppController.getInstance().addToRequestQueue(accountInfoRequest, tag_user_data_json);
    }
}
