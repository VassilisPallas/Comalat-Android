package org.sakaiproject.api.memberships;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.Membership;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vasilis on 11/21/15.
 */
public class MembershipService {
    private Context context;

    private final String membership_tag = User.getUserEid() + " memberships";
    private String membership_id_tag, membership_page_tag, membership_perms_tag, membership_user_perms_tag;
    private Gson gson = new Gson();
    private Callback callback;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private boolean login = false;

    /**
     * OnlineSite constructor
     *
     * @param context the context
     */
    public MembershipService(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setDelegate(Callback callback) {
        this.callback = callback;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * REST calls to get the data from sites and projects the user has join
     * http://141.99.248.86:8089/direct/membership.json
     *
     * @param url the url
     * @throws IOException
     */
    public void getSites(String url) throws IOException {

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest membershipRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Membership membership = gson.fromJson(response.toString(), Membership.class);

                JsonParser.parseSiteDataJson(membership);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "projectsAndSitesJson", User.getUserEid() + File.separator + "memberships");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                // starting from 1 because 0 is My Workspace
                for (int i = 1; i < SiteData.getSites().size(); i++) {
                    getData(SiteData.getSites().get(i), null, i);
                }

                for (int i = 0; i < SiteData.getProjects().size(); i++) {
                    getData(SiteData.getProjects().get(i), "project", i);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        AppController.getInstance().addToRequestQueue(membershipRequest, membership_tag);
    }

    private void getData(final SiteData siteData, final String type, final int index) {
        membership_id_tag = User.getUserEid() + " membership " + siteData.getId() + " data";

        final JsonObjectRequest membershipDataRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + siteData.getId() + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MembershipData membershipData = gson.fromJson(response.toString(), MembershipData.class);

                JsonParser.getSiteData(membershipData, index, type);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId()))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteData.getId(), User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getPages(siteData, type, index);
                getPermissions(siteData, type, index);
                getUserPermissions(siteData, type, index);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        AppController.getInstance().addToRequestQueue(membershipDataRequest, membership_id_tag);
    }

    private void getPages(final SiteData siteData, final String type, final int index) {
        membership_page_tag = User.getUserEid() + " membership " + siteData.getId() + " pages";

        JsonArrayRequest pagesRequest = new JsonArrayRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + siteData.getId() + "/pages.json", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type collectionType = new TypeToken<List<SitePage>>() {
                }.getType();
                List<SitePage> pages = gson.fromJson(response.toString(), collectionType);

                JsonParser.getSitePageData(pages, index, type);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId()))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteData.getId() + "_pages", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });

        AppController.getInstance().addToRequestQueue(pagesRequest, membership_page_tag);
    }

    private void getPermissions(final SiteData siteData, final String type, final int index) {

        membership_perms_tag = User.getUserEid() + " membership " + siteData.getId() + " perms";

        JsonObjectRequest pagePermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + siteData.getId() + "/perms.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PagePermissions pagePermissions = gson.fromJson(response.toString(), PagePermissions.class);
                JsonParser.getSitePermissions(pagePermissions, index, type);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId()))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteData.getId() + "_perms", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        AppController.getInstance().addToRequestQueue(pagePermissionsRequest, membership_perms_tag);
    }

    private void getUserPermissions(final SiteData siteData, final String type, final int index) {
        membership_user_perms_tag = User.getUserEid() + " membership " + siteData.getId() + " user perms";
        JsonObjectRequest pageUserPermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + siteData.getId() + "/userPerms.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PageUserPermissions pageUserPermissions = gson.fromJson(response.toString(), PageUserPermissions.class);
                JsonParser.getUserSitePermissions(pageUserPermissions, index, type);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId()))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteData.getId() + "_userPerms", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (index == SiteData.getProjects().size() - 1 && type != null) {
                    if (login) {
                        Intent i = new Intent(context, UserActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        ((AppCompatActivity) context).finish();
                    } else {
                        if (callback != null)
                            callback.onSuccess(null);

                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        AppController.getInstance().addToRequestQueue(pageUserPermissionsRequest, membership_user_perms_tag);
    }
}
