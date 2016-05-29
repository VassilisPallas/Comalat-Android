package org.sakaiproject.api.memberships.pages.announcements;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.announcements.Announcement;
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
 * Created by vspallas on 09/02/16.
 */
public class MembershipAnnouncementsService {

    private Context context;
    private String siteId;
    private final String membership_announcement_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;

    public MembershipAnnouncementsService(Context context, String id, Callback callback) {
        this.context = context;
        siteId = id;
        this.callback = callback;
        membership_announcement_tag = User.getUserEid() + " " + id + " announcements";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAnnouncements(String url) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest announcementsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Announcement announcement = gson.fromJson(response.toString(), Announcement.class);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "announcements"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_announcements", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "announcements");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                callback.onSuccess(announcement);

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
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
        AppController.getInstance().addToRequestQueue(announcementsRequest, membership_announcement_tag);
    }
}
