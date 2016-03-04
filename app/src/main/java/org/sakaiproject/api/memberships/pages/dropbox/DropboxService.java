package org.sakaiproject.api.memberships.pages.dropbox;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.sync.DropboxRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public class DropboxService {
    private Context context;
    private SiteData siteData;
    private Gson gson = new Gson();
    private LinkedHashMap<Member, Dropbox> dropboxList = new LinkedHashMap<>();
    private DropboxRefreshUI callback;
    private Roster roster;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public DropboxService(Context context, SiteData siteData, DropboxRefreshUI callback, Roster roster, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.siteData = siteData;
        this.callback = callback;
        this.roster = roster;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void getDropboxItems() {
        swipeRefreshLayout.setRefreshing(true);
        for (int i = 0; i < roster.getMembersTotal(); i++) {
            Member member = roster.getMembers().get(i);
            String url = context.getResources().getString(R.string.url) + "dropbox/site/" + siteData.getId() + "/user/" + member.getEid() + ".json";
            String tag = User.getUserEid() + " " + siteData.getId() + " " + member.getEid() + " dropbox";
            getItem(url, tag, i, member);
        }
    }

    private void getItem(String url, String tag, final int index, final Member member) {
        JsonObjectRequest dropboxRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Dropbox dropbox = gson.fromJson(response.toString(), Dropbox.class);

                if (dropbox.getCollection() != null) {
                    dropboxList.put(member, dropbox);
                    // save on internal storage
                }
                if (index == roster.getMembersTotal() - 1)
                    callback.updateUI(dropboxList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError)
                    Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                if (index == roster.getMembersTotal() - 1)
                    callback.updateUI(dropboxList);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(dropboxRequest, tag);
    }

}
