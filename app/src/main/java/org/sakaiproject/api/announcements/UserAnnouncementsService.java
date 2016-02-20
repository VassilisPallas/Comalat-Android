package org.sakaiproject.api.announcements;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.api.pojos.announcements.SiteName;
import org.sakaiproject.api.sync.AnnouncementRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Actions;
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

        JsonObjectRequest userAnnouncementsRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                announcementsJson = response.toString();
                announcement = gson.fromJson(response.toString(), Announcement.class);

                if (announcement.getAnnouncementCollection().size() > 0) {

                    for (int i = 0; i < announcement.getAnnouncementCollection().size(); i++) {

                        final int index = i;

                        Announcement.AnnouncementItems item = announcement.getAnnouncementCollection().get(i);

                        site_name_tag = User.getUserEid() + " " + item.getSiteId() + " name";
                        JsonObjectRequest siteNameRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + item.getSiteId() + ".json", null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                SiteName siteName = gson.fromJson(response.toString(), SiteName.class);

                                announcement.getAnnouncementCollection().get(index).setSiteTitle(siteName.getEntityTitle());

                                JsonParser.getUserAnnouncements(announcement);

                                announcementsJson = gson.toJson(announcement);

                                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements"))
                                    try {
                                        Actions.writeJsonFile(context, announcementsJson, "announcements", User.getUserEid() + File.separator + "announcements");
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
                                VolleyLog.d(site_name_tag, error.getMessage());
                                if (swipeRefreshLayout != null)
                                    swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        AppController.getInstance().addToRequestQueue(siteNameRequest, site_name_tag);
                    }
                } else {
                    if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements"))
                        try {
                            Actions.writeJsonFile(context, announcementsJson, "announcements", User.getUserEid() + File.separator + "announcements");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    delegate.updateUI();

                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(user_announcement_tag, error.getMessage());
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(userAnnouncementsRequest, user_announcement_tag);
    }
}
