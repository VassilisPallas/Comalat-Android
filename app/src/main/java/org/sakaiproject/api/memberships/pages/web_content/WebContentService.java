package org.sakaiproject.api.memberships.pages.web_content;

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
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.web_content.WebContent;
import org.sakaiproject.api.sync.WebContentRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 01/03/16.
 */
public class WebContentService {
    Context context;
    Gson gson = new Gson();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteId;
    private WebContent webContent;
    private WebContentRefreshUI callback;
    private final String web_content_tag;

    public WebContentService(Context context, CustomSwipeRefreshLayout swipeRefreshLayout, String siteId, WebContentRefreshUI callback) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.siteId = siteId;
        this.callback = callback;

        web_content_tag = User.getUserEid() + " " + siteId + " web content";

    }

    public void getWebContent(String url) {
        swipeRefreshLayout.setRefreshing(true);
        final JsonObjectRequest webContentRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                webContent = gson.fromJson(response.toString(), WebContent.class);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "web_content"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), siteId + "_web_content", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "web_content");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                callback.updateUI(webContent);

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
        AppController.getInstance().addToRequestQueue(webContentRequest, web_content_tag);
    }
}
