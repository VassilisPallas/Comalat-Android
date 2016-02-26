package org.sakaiproject.api.assignments;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.SiteName;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.sync.AssignmentsRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 22/02/16.
 */
public class UserAssignmentsService {
    private Context context;
    private final String user_assignments_tag;
    private String site_name_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private AssignmentsRefreshUI delegate;
    private boolean update = false;

    public UserAssignmentsService(Context context, AssignmentsRefreshUI delegate) {
        this.context = context;
        this.delegate = delegate;
        user_assignments_tag = User.getUserEid() + " assignments";
    }

    public void canUpdate(boolean canUpdate) {
        this.update = canUpdate;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAssignments(String url) {
        JsonObjectRequest assignmentsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Assignment assignment = gson.fromJson(response.toString(), Assignment.class);
                JsonParser.getMembershipAssignments(assignment);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "assignments"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "assignments", User.getUserEid() + File.separator + "assignments");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (assignment.getAssignmentsCollectionList().size() > 0) {
                    for (int i = 0; i < assignment.getAssignmentsCollectionList().size(); i++) {
                        site_name_tag = User.getUserEid() + " " + assignment.getAssignmentsCollectionList().get(i).getContext() + " assignment name";

                        final Assignment.AssignmentsCollection collection = assignment.getAssignmentsCollectionList().get(i);

                        JsonObjectRequest siteNameRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + collection.getContext() + ".json", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                SiteName siteName = gson.fromJson(response.toString(), SiteName.class);

                                JsonParser.getAssignmentSiteName(context, siteName, collection);

                                if (update) {
                                    delegate.updateUI();

                                    if (swipeRefreshLayout != null)
                                        swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(site_name_tag, error.getMessage());
                                if (swipeRefreshLayout != null)
                                    swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        AppController.getInstance().addToRequestQueue(siteNameRequest, site_name_tag);
                    }

                } else {
                    if (update) {
                        delegate.updateUI();

                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(user_assignments_tag, error.getMessage());
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(assignmentsRequest, user_assignments_tag);
    }
}
