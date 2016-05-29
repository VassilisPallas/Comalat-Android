package org.sakaiproject.api.user.workspace;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.memberships.MembershipService;
import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 18/03/16.
 */
public class WorkspaceService {
    private String id;
    private Gson gson = new Gson();
    private Context context;

    private ProgressBar progressBar;
    private TextView loginTextView;

    private Callback callback;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    private boolean login = false;

    public WorkspaceService(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setLoginTextView(TextView loginTextView) {
        this.loginTextView = loginTextView;
    }

    public void setDelegate(Callback delegate) {
        this.callback = delegate;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void getWorkspace() {
        JsonObjectRequest getWorkSpaceRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/~" + id + ".json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MembershipData membershipData = gson.fromJson(response.toString(), MembershipData.class);

                JsonParser.getSiteData(membershipData);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "my_workspace"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), id, User.getUserEid() + File.separator + "my_workspace");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                getPages();
                getPermissions();
                getUserPermissions();

                MembershipService membershipService = new MembershipService(context);
                membershipService.setDelegate(callback);
                membershipService.setSwipeRefreshLayout(swipeRefreshLayout);
                membershipService.setLogin(login);
                try {
                    membershipService.getSites(context.getString(R.string.url) + "membership.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar != null && loginTextView != null) {
                    progressBar.setVisibility(View.GONE);
                    loginTextView.setVisibility(View.VISIBLE);
                }
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

        AppController.getInstance().addToRequestQueue(getWorkSpaceRequest, id + "_workspace");
    }

    private void getPages() {
        JsonArrayRequest getWorkspacePagesRequest = new JsonArrayRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/~" + id + "/pages.json", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type collectionType = new TypeToken<List<SitePage>>() {
                }.getType();
                List<SitePage> pages = gson.fromJson(response.toString(), collectionType);

                JsonParser.getSitePageData(pages);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "my_workspace"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), id + "_pages", User.getUserEid() + File.separator + "my_workspace");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar != null && loginTextView != null) {
                    progressBar.setVisibility(View.GONE);
                    loginTextView.setVisibility(View.VISIBLE);
                }

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
        AppController.getInstance().addToRequestQueue(getWorkspacePagesRequest, id + "_workspace_pages");
    }

    private void getPermissions() {
        JsonObjectRequest getWorkspacePermissions = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/~" + id + "/perms.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                PagePermissions pagePermissions = gson.fromJson(response.toString(), PagePermissions.class);
                JsonParser.getSitePermissions(pagePermissions);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "my_workspace"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), id + "_perms", User.getUserEid() + File.separator + "my_workspace");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar != null && loginTextView != null) {
                    progressBar.setVisibility(View.GONE);
                    loginTextView.setVisibility(View.VISIBLE);
                }

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
        AppController.getInstance().addToRequestQueue(getWorkspacePermissions, id + "_workspace_perms");
    }

    private void getUserPermissions() {
        JsonObjectRequest getWorkspaceUserPermissions = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/~" + id + "/userPerms.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                PageUserPermissions pageUserPermissions = gson.fromJson(response.toString(), PageUserPermissions.class);
                JsonParser.getUserSitePermissions(pageUserPermissions);

                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "my_workspace"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), id + "_userPerms", User.getUserEid() + File.separator + "my_workspace");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar != null && loginTextView != null) {
                    progressBar.setVisibility(View.GONE);
                    loginTextView.setVisibility(View.VISIBLE);
                }

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
        AppController.getInstance().addToRequestQueue(getWorkspaceUserPermissions, id + "_workspace_userPerms");
    }
}
