package org.sakaiproject.api.memberships;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.Membership;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.api.sync.MembershipRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Actions;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

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
    private MembershipRefreshUI delegate;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    /**
     * OnlineSite constructor
     *
     * @param context the context
     */
    public MembershipService(Context context, MembershipRefreshUI delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * REST calls to get the data from sites and projects the user has join
     * http://141.99.248.86:8089/direct/membership.json
     *
     * @param url the url
     * @throws IOException
     */
    public void getSites(String url) throws IOException {

        SiteData.getSites().clear();
        SiteData.getProjects().clear();

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest membershipRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Membership membership = gson.fromJson(response.toString(), Membership.class);

                JsonParser.parseSiteDataJson(membership);

                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships"))
                    try {
                        Actions.writeJsonFile(context, response.toString(), "projectsAndSitesJson", User.getUserEid() + File.separator + "memberships");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                for (int i = 0; i < SiteData.getSites().size(); i++) {

                    final int index = i;

                    membership_id_tag = User.getUserEid() + " membership " + SiteData.getSites().get(i).getId() + " data";

                    JsonObjectRequest siteDataRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            MembershipData membershipData = gson.fromJson(response.toString(), MembershipData.class);

                            JsonParser.getSiteData(membershipData, index, null);
                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getSites().get(index).getId(), User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_id_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    })

                    {
                        @Override
                        public Priority getPriority() {
                            return Priority.IMMEDIATE;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(siteDataRequest, membership_id_tag);


                    membership_page_tag = User.getUserEid() + " membership " + SiteData.getSites().get(i).getId() + " pages";

                    JsonArrayRequest pagesRequest = new JsonArrayRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/pages.json", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Type collectionType = new TypeToken<List<SitePage>>() {
                            }.getType();
                            List<SitePage> pages = gson.fromJson(response.toString(), collectionType);

                            JsonParser.getSitePageData(pages, index, null);

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getSites().get(index).getId() + "_pages", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_page_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pagesRequest, membership_page_tag);


                    membership_perms_tag = User.getUserEid() + " membership " + SiteData.getSites().get(i).getId() + " perms";

                    JsonObjectRequest pagePermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/perms.json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            PagePermissions pagePermissions = gson.fromJson(response.toString(), PagePermissions.class);
                            JsonParser.getSitePermissions(pagePermissions, index, null);

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getSites().get(index).getId() + "_perms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_perms_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pagePermissionsRequest, membership_perms_tag);

                    membership_user_perms_tag = User.getUserEid() + " membership " + SiteData.getSites().get(i).getId() + " user perms";
                    JsonObjectRequest pageUserPermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/userPerms.json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            PageUserPermissions pageUserPermissions = gson.fromJson(response.toString(), PageUserPermissions.class);
                            JsonParser.getUserSitePermissions(pageUserPermissions, index, null);

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getSites().get(index).getId() + "_userPerms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_user_perms_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pageUserPermissionsRequest, membership_user_perms_tag);

                }

                for (int i = 0; i < SiteData.getProjects().size(); i++) {

                    membership_id_tag = User.getUserEid() + " membership " + SiteData.getProjects().get(i).getId() + " data";

                    final int index = i;

                    JsonObjectRequest siteDateRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            MembershipData membershipData = gson.fromJson(response.toString(), MembershipData.class);

                            JsonParser.getSiteData(membershipData, index, "project");
                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getProjects().get(index).getId(), User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_id_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }) {
                        @Override
                        public Priority getPriority() {
                            return Priority.IMMEDIATE;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(siteDateRequest, membership_id_tag);


                    membership_page_tag = User.getUserEid() + " membership " + SiteData.getProjects().get(i).getId() + " pages";

                    JsonArrayRequest pagesRequest = new JsonArrayRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/pages.json", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            Type collectionType = new TypeToken<List<SitePage>>() {
                            }.getType();
                            List<SitePage> pages = gson.fromJson(response.toString(), collectionType);


                            JsonParser.getSitePageData(pages, index, "project");

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getProjects().get(index).getId() + "_pages", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_page_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pagesRequest, membership_page_tag);


                    membership_perms_tag = User.getUserEid() + " membership " + SiteData.getProjects().get(i).getId() + " perms";

                    JsonObjectRequest pagePermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/perms.json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            PagePermissions pagePermissions = gson.fromJson(response.toString(), PagePermissions.class);
                            JsonParser.getSitePermissions(pagePermissions, index, "project");

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getProjects().get(index).getId() + "_perms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_perms_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pagePermissionsRequest, membership_perms_tag);

                    membership_user_perms_tag = User.getUserEid() + " membership " + SiteData.getProjects().get(i).getId() + " user perms";
                    JsonObjectRequest pageUserPermissionsRequest = new JsonObjectRequest(Request.Method.GET, context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/userPerms.json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            PageUserPermissions pageUserPermissions = gson.fromJson(response.toString(), PageUserPermissions.class);
                            JsonParser.getUserSitePermissions(pageUserPermissions, index, "project");

                            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId()))
                                try {
                                    Actions.writeJsonFile(context, response.toString(), SiteData.getProjects().get(index).getId() + "_userPerms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(index).getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            if (index == SiteData.getProjects().size() - 1) {
                                delegate.updateUI();
                                if (swipeRefreshLayout != null)
                                    swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(membership_user_perms_tag, error.getMessage());
                            if (swipeRefreshLayout != null)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(pageUserPermissionsRequest, membership_user_perms_tag);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(membership_tag, error.getMessage());
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(membershipRequest, membership_tag);
    }
}
