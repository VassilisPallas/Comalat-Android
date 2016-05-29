package org.sakaiproject.api.memberships.pages.roster;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.pojos.roster.UserProfileImage;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 02/03/16.
 */
public class RosterService {
    private Context context;
    private SiteData siteData;
    private final String roster_tag;
    private Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;

    private JsonObjectRequest userProfileImageUrlRequest = null;

    public RosterService(Context context, SiteData siteData, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout, Callback callback) {
        this.context = context;
        this.siteData = siteData;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.callback = callback;
        roster_tag = User.getUserEid() + " " + siteData.getId() + " roster";
    }

    public void getRoster(String url) {
        swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest rosterRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Roster roster = gson.fromJson(response.toString(), Roster.class);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteData.getId() + "_roster", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                for (int i = 0; i < roster.getMembers().size(); i++) {
                    Member member = roster.getMembers().get(i);
                    getUserProfile(member.getUserId(), roster, i);
                }

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

        AppController.getInstance().addToRequestQueue(rosterRequest, roster_tag);
    }

    private void getUserProfile(final String userId, final Roster roster, final int index) {
        final String tag_user_image_url = userId + " image url";
        String url = context.getResources().getString(R.string.url) + "profile/" + userId + ".json";
        userProfileImageUrlRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserProfileImage userProfileImage = gson.fromJson(response.toString(), UserProfileImage.class);
                getUserProfileImage(userId, userProfileImage.getImageThumbUrl(), roster, index);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int tempIndex = index;
                Roster tempRoster = roster;
                String tempUserId = userId;
                getUserProfile(tempUserId, tempRoster, tempIndex);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(userProfileImageUrlRequest, tag_user_image_url);
    }

    private void getUserProfileImage(final String userId, String url, final Roster roster, final int index) {

        String tag_user_image = userId + " image";

        ImageRequest userImage = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster" + File.separator + "user_images"))
                    try {
                        ActionsHelper.saveImage(context, response, userId + "_image.jpg", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster" + File.separator + "user_images");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                callback.onSuccess(roster);

            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
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
        AppController.getInstance().addToRequestQueue(userImage, tag_user_image);
    }
}
