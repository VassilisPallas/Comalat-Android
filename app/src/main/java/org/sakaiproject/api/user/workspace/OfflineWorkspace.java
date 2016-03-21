package org.sakaiproject.api.user.workspace;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.memberships.OfflineMemberships;
import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vspallas on 19/03/16.
 */
public class OfflineWorkspace {
    private String id;
    private Gson gson = new Gson();
    private Context context;

    private ProgressBar progressBar;
    private TextView loginTextView;

    private Callback callback;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    private boolean login = false;

    public OfflineWorkspace(Context context, String id) {
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
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "my_workspace")) {
            try {
                String json = ActionsHelper.readJsonFile(context, id, User.getUserEid() + File.separator + "my_workspace");

                MembershipData membershipData = gson.fromJson(json, MembershipData.class);
                JsonParser.getSiteData(membershipData);

                getPages();
                getPermissions();
                getUserPermissions();

                OfflineMemberships offlineSites = new OfflineMemberships(context);
                offlineSites.setLogin(login);
                offlineSites.getSites();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPages() throws IOException {
        String json = ActionsHelper.readJsonFile(context, id + "_pages", User.getUserEid() + File.separator + "my_workspace");

        Type collectionType = new TypeToken<List<SitePage>>() {
        }.getType();
        List<SitePage> pages = gson.fromJson(json, collectionType);

        JsonParser.getSitePageData(pages);
    }

    private void getPermissions() throws IOException {
        String json = ActionsHelper.readJsonFile(context, id + "_perms", User.getUserEid() + File.separator + "my_workspace");
        PagePermissions pagePermissions = gson.fromJson(json, PagePermissions.class);
        JsonParser.getSitePermissions(pagePermissions);
    }

    private void getUserPermissions() throws IOException {
        String json = ActionsHelper.readJsonFile(context, id + "_userPerms", User.getUserEid() + File.separator + "my_workspace");
        PageUserPermissions pageUserPermissions = gson.fromJson(json, PageUserPermissions.class);
        JsonParser.getUserSitePermissions(pageUserPermissions);
    }
}
