package org.sakaiproject.api.session;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 10/28/15.
 */
public class RefreshSession {

    private Context context;
    private final String refresh_session_tag = User.getUserEid() + " session refresh";
    private JSONObject jsonBody;

    /**
     * RefreshSession constructor
     *
     * @param context the context
     */
    public RefreshSession(Context context) {
        this.context = context;
    }

    public void putSession(String url) {

        String loginJson = null;
        jsonBody = null;
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user"))
            try {
                loginJson = ActionsHelper.readJsonFile(context, "loginJson", User.getUserEid() + File.separator + "user");
                jsonBody = new JSONObject(loginJson);

                Log.i("json", jsonBody.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        EmptyRequest refreshSessionRequest = new EmptyRequest(Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("session", "stored");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("session", "didn't stored");
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };


        refreshSessionRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(refreshSessionRequest, refresh_session_tag);
    }
}
