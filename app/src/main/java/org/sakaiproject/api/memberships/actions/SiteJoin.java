package org.sakaiproject.api.memberships.actions;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.sakai.AppController;

/**
 * Created by vasilis on 1/22/16.
 */
public class SiteJoin {

    public void join(String url) {

        EmptyRequest membershipJoinRequest = new EmptyRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("join", error.getMessage());
                Log.i("join", "false");
            }
        });

        AppController.getInstance().addToRequestQueue(membershipJoinRequest, "join");
    }
}
