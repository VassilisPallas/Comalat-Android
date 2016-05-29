package org.sakaiproject.api.memberships.pages.wiki;

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
import org.sakaiproject.api.pojos.wiki.Wiki;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 28/02/16.
 */
public class MembershipWikiService {
    Context context;
    Gson gson = new Gson();
    private Wiki wiki;
    private final String wiki_tag = User.getUserEid() + " wiki";
    private final String wiki_page_data = User.getUserEid() + " wiki page data";
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Callback callback;
    private String siteId;

    public MembershipWikiService(Context context, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout, Callback callback, String siteId) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.callback = callback;
        this.siteId = siteId;
    }

    public void getWiki(final String url) {
        swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest wikiRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                wiki = gson.fromJson(response.toString(), Wiki.class);

                if (siteId != null) {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki"))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_wiki", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                } else {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "wiki"))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), "wiki", User.getUserEid() + File.separator + "wiki");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                getPageData(url.replaceFirst(".json", "") + "/page/" + wiki.getName() + ".json");

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

        AppController.getInstance().addToRequestQueue(wikiRequest, wiki_tag);
    }

    private void getPageData(String url) {
        JsonObjectRequest wikiPageRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Wiki temp = gson.fromJson(response.toString(), Wiki.class);
                wiki.setComments(temp.getComments());
                wiki.setHtml(temp.getHtml());

                if (siteId != null) {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki"))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_wiki_data", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                } else {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "wiki"))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), "wiki_data", User.getUserEid() + File.separator + "wiki");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                callback.onSuccess(wiki);
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

        AppController.getInstance().addToRequestQueue(wikiPageRequest, wiki_page_data);
    }
}
