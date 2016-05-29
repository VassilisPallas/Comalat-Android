package org.sakaiproject.api.assignments;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.SiteName;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 22/02/16.
 */
public class UserAssignmentsService {
    private Context context;
    private final String user_assignments_tag;
    private String site_name_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;

    public UserAssignmentsService(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        user_assignments_tag = User.getUserEid() + " assignments";
    }


    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAssignments(String url) {
        JsonObjectRequest assignmentsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Assignment assignment = gson.fromJson(response.toString(), Assignment.class);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "assignments"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "assignments", User.getUserEid() + File.separator + "assignments");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (assignment.getAssignmentsCollectionList().size() > 0) {
                    for (int i = 0; i < assignment.getAssignmentsCollectionList().size(); i++) {
                        final Assignment.AssignmentsCollection collection = assignment.getAssignmentsCollectionList().get(i);
                        getSiteName(collection, assignment);
                    }

                } else {
                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                }
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
        AppController.getInstance().addToRequestQueue(assignmentsRequest, user_assignments_tag);
    }

    private void getSiteName(final Assignment.AssignmentsCollection collection, final Assignment assignment) {
        site_name_tag = User.getUserEid() + " " + collection.getContext() + " assignment name";
        JsonObjectRequest siteNameRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + collection.getContext() + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                SiteName siteName = gson.fromJson(response.toString(), SiteName.class);

                JsonParser.getAssignmentSiteName(context, siteName, collection);

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
        AppController.getInstance().addToRequestQueue(siteNameRequest, site_name_tag);
    }
}
