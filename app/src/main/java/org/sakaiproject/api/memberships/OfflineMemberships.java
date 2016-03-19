package org.sakaiproject.api.memberships;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.Membership;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.sakai.UserActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vasilis on 1/11/16.
 */
public class OfflineMemberships {

    private Context context;
    private Gson gson = new Gson();
    private boolean login = false;

    /**
     * OfflineSite constructor
     *
     * @param context the context
     */
    public OfflineMemberships(Context context) {
        this.context = context;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * parse the data from sites and projects the user has join
     *
     * @throws IOException
     */
    public void getSites() throws IOException {
        String sitesJson = null;
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships")) {
            sitesJson = ActionsHelper.readJsonFile(context, "projectsAndSitesJson", User.getUserEid() + File.separator + "memberships");

            Membership membership = gson.fromJson(sitesJson, Membership.class);
            JsonParser.parseSiteDataJson(membership);
        }

        for (int i = 1; i < SiteData.getSites().size(); i++) {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getSites().get(i).getId(), User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
                MembershipData membershipData = gson.fromJson(sitesJson, MembershipData.class);
                JsonParser.getSiteData(membershipData, i, null);
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getSites().get(i).getId() + "_pages", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
                Type collectionType = new TypeToken<List<SitePage>>() {
                }.getType();
                List<SitePage> pages = gson.fromJson(sitesJson, collectionType);
                JsonParser.getSitePageData(pages, i, null);
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getSites().get(i).getId() + "_perms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
                PagePermissions pagePermissions = gson.fromJson(sitesJson, PagePermissions.class);
                JsonParser.getSitePermissions(pagePermissions, i, null);
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getSites().get(i).getId() + "_userPerms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
                PageUserPermissions pageUserPermissions = gson.fromJson(sitesJson, PageUserPermissions.class);
                JsonParser.getUserSitePermissions(pageUserPermissions, i, null);
            }
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            int x = SiteData.getProjects().size();

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getProjects().get(i).getId(), User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
                MembershipData membershipData = gson.fromJson(sitesJson, MembershipData.class);
                JsonParser.getSiteData(membershipData, i, "project");
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_pages", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
                Type collectionType = new TypeToken<List<SitePage>>() {
                }.getType();
                List<SitePage> pages = gson.fromJson(sitesJson, collectionType);
                JsonParser.getSitePageData(pages, i, "project");
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_perms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
                PagePermissions pagePermissions = gson.fromJson(sitesJson, PagePermissions.class);
                JsonParser.getSitePermissions(pagePermissions, i, "project");
            }

            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId())) {
                sitesJson = ActionsHelper.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_userPerms", User.getUserEid() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
                PageUserPermissions pageUserPermissions = gson.fromJson(sitesJson, PageUserPermissions.class);
                JsonParser.getUserSitePermissions(pageUserPermissions, i, "project");
            }

            if (i == SiteData.getProjects().size() - 1 && login) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
            }
        }
    }
}
