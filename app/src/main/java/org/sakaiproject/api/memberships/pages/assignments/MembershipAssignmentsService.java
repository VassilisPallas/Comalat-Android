package org.sakaiproject.api.memberships.pages.assignments;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.assignments.Assignment;
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
 * Created by vspallas on 22/02/16.
 */
public class MembershipAssignmentsService {
    private Context context;
    private String siteId;
    private final String membership_assignments_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;

    public MembershipAssignmentsService(Context context, String siteId, Callback callback) {
        this.context = context;
        this.siteId = siteId;
        this.callback = callback;
        membership_assignments_tag = User.getUserEid() + " " + siteId + " announcements";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAssignments(String url) {
        JsonObjectRequest assignmentsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Assignment assignment = gson.fromJson(response.toString(), Assignment.class);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_assignments", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (callback != null)
                    callback.onSuccess(assignment);

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(assignmentsRequest, membership_assignments_tag);
    }
}
