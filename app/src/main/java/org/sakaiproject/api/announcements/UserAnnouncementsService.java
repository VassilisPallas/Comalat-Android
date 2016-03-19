package org.sakaiproject.api.announcements;

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
import org.sakaiproject.api.pojos.SiteName;
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
public class UserAnnouncementsService {

    private Context context;
    private final String user_announcement_tag = User.getUserEid() + " announcements";
    private String site_name_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private AnnouncementRefreshUI delegate;
    private Announcement announcement;
    private String announcementsJson;

    public UserAnnouncementsService(Context context, AnnouncementRefreshUI delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getAnnouncements(String url) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest userAnnouncementsRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                announcementsJson = response.toString();
                announcement = gson.fromJson(response.toString(), Announcement.class);

                if (announcement.getAnnouncementCollection().size() > 0) {

                    for (int i = 0; i < announcement.getAnnouncementCollection().size(); i++) {

                        Announcement.AnnouncementItems item = announcement.getAnnouncementCollection().get(i);
                        getSiteName(item, i);
                    }
                } else {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements"))
                        try {
                            ActionsHelper.writeJsonFile(context, announcementsJson, "announcements", User.getUserEid() + File.separator + "announcements");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    delegate.updateUI(announcement);

                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                }
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
        AppController.getInstance().addToRequestQueue(userAnnouncementsRequest, user_announcement_tag);
    }


    private void getSiteName(Announcement.AnnouncementItems item, final int index) {
        site_name_tag = User.getUserEid() + " " + item.getSiteId() + " name";
        JsonObjectRequest siteNameRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + item.getSiteId() + ".json", (String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                SiteName siteName = gson.fromJson(response.toString(), SiteName.class);

                // change value MyWorkspace with the real name of the membership
                announcement.getAnnouncementCollection().get(index).setSiteTitle(siteName.getEntityTitle());

                // make String with the "new" json
                announcementsJson = gson.toJson(announcement);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements"))
                    try {
                        ActionsHelper.writeJsonFile(context, announcementsJson, "announcements", User.getUserEid() + File.separator + "announcements");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                delegate.updateUI(announcement);

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
        AppController.getInstance().addToRequestQueue(siteNameRequest, site_name_tag);
    }
}
