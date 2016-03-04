package org.sakaiproject.api.memberships.pages.announcements;

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
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.api.sync.AnnouncementRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 09/02/16.
 */
public class MembershipAnnouncementsService {

    private Context context;
    private String siteId;
    private final String membership_announcement_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private AnnouncementRefreshUI delegate;

    public MembershipAnnouncementsService(Context context, String id, AnnouncementRefreshUI delegate) {
        this.context = context;
        siteId = id;
        this.delegate = delegate;
        membership_announcement_tag = User.getUserEid() + " " + id + " announcements";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAnnouncements(String url) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest announcementsRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Announcement announcement = gson.fromJson(response.toString(), Announcement.class);
                JsonParser.getMembershipAnnouncements(announcement);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "announcements"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_announcements", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "announcements");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                delegate.updateUI();

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
        AppController.getInstance().addToRequestQueue(announcementsRequest, membership_announcement_tag);
    }
}
