package org.sakaiproject.api.memberships.pages.wiki;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.pojos.wiki.Wiki;
import org.sakaiproject.api.sync.WikiRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;

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
    private WikiRefreshUI callback;
    private String siteId;

    public MembershipWikiService(Context context, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout, WikiRefreshUI callback, String siteId) {
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

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_wiki", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getPageData(url.replaceFirst(".json", "") + "/page/" + wiki.getName() + ".json");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(wiki_tag, error);
            }
        });

        AppController.getInstance().addToRequestQueue(wikiRequest, wiki_tag);
    }

    private void getPageData(String url) {
        JsonObjectRequest wikiPageRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Wiki temp = gson.fromJson(response.toString(), Wiki.class);
                wiki.setComments(temp.getComments());
                wiki.setHtml(temp.getHtml());

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_wiki_data", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "wiki");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                callback.updateUI(wiki);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(wiki_page_data, error);
            }
        });

        AppController.getInstance().addToRequestQueue(wikiPageRequest, wiki_page_data);
    }
}
