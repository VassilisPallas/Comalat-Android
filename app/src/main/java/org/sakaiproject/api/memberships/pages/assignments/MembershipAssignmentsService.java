package org.sakaiproject.api.memberships.pages.assignments;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.json.JsonParser;
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
public class MembershipAssignmentsService {
    private Context context;
    private String siteId;
    private final String membership_assignments_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private AssignmentsRefreshUI delegate;

    public MembershipAssignmentsService(Context context, String siteId, AssignmentsRefreshUI delegate) {
        this.context = context;
        this.siteId = siteId;
        this.delegate = delegate;
        membership_assignments_tag = User.getUserEid() + " " + siteId + " announcements";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAssignments(String url) {
        JsonObjectRequest assignmentsRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Assignment assignment = gson.fromJson(response.toString(), Assignment.class);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_assignments", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (delegate != null)
                    delegate.updateUI(assignment);

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(assignmentsRequest, membership_assignments_tag);
    }
}
