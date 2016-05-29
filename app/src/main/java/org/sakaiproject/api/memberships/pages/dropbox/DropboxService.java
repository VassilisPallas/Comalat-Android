package org.sakaiproject.api.memberships.pages.dropbox;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
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
 * Created by vspallas on 03/03/16.
 */
public class DropboxService {
    private Context context;
    private SiteData siteData;
    private Gson gson = new Gson();
    private Map<String, Integer> dropboxList = new HashMap<>();
    private Callback callback;
    private Roster roster;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public DropboxService(Context context, SiteData siteData, Callback callback, Roster roster, org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
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

                if (dropbox != null && dropbox.getCollection() != null) {

                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox"))
                        try {
                            ActionsHelper.writeJsonFile(context, response.toString(), member.getUserId() + "_dropbox", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    for (int i = 0; i < dropbox.getCollection().size(); i++) {
                        String url = dropbox.getCollection().get(i).getUrl();
                        File externalStoragePath = context.getFilesDir();
                        String tempUrl = (context.getResources().getString(R.string.url).replaceFirst("direct", "access")) + "content/group-user/" + siteData.getId();
                        url = url.replaceFirst(tempUrl, "");
                        url = url.replaceAll(member.getUserId(), member.getEid());
                        url = User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox" + File.separator + "files" + url;
                        File f = new File(externalStoragePath, url);
                        if (!f.exists()) {
                            try {
                                f.getParentFile().mkdirs();
                                f.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        dropboxList.put(f.getAbsolutePath(), dropbox.getCollection().get(i).getSize());
                    }
                }
                if (index == roster.getMembersTotal() - 1) {
                    callback.onSuccess(dropboxList);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
                if (index == roster.getMembersTotal() - 1) {
                    callback.onSuccess(dropboxList);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(dropboxRequest, tag);
    }

}
