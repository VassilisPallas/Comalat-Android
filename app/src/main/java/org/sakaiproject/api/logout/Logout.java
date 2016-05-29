package org.sakaiproject.api.logout;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.sakai.AppController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 10/25/15.
 */
public class Logout {

    private final String logout_tag = User.getUserEid() + " logout";

    public Logout() {
    }

    public void logout(String url) {

        StringRequest logoutrequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        logoutrequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(logoutrequest, logout_tag);
    }

}
