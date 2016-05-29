package org.sakaiproject.api.memberships.actions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;
import org.sakaiproject.customviews.custom_volley.EmptyRequest;
import org.sakaiproject.general.Connection;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasilis on 1/22/16.
 */
public class SiteJoin {

    public void join(String url, final Context context) {

        EmptyRequest membershipJoinRequest = new EmptyRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(membershipJoinRequest, "join");
    }
}
