package org.sakaiproject.api.site;

import android.content.Context;

import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.api.json.JsonParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 1/11/16.
 */
public class OfflineSite {

    private Context context;
    private JsonParser jsonParse;

    /**
     * OfflineSite constructor
     *
     * @param context the context
     */
    public OfflineSite(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
    }

    /**
     * parse the data from sites and projects the user has join
     *
     * @throws IOException
     */
    public void getSites() throws IOException {
        String sitesJson = null;
        if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships"))
            sitesJson = Actions.readJsonFile(context, "projectsAndSitesJson", User.getUserId() + File.separator + "memberships");
        jsonParse.parseSiteDataJson(sitesJson);

        for (int i = 0; i < SiteData.getSites().size(); i++) {
            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId(), User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
            jsonParse.getSiteData(sitesJson, i);

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_pages", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
            jsonParse.getSitePageData(sitesJson, i, "site");

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_perms", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
            jsonParse.getSitePermissions(sitesJson, i, "site");

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_userPerms", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getSites().get(i).getId());
            jsonParse.getUserSitePermissions(sitesJson, i, "site");
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId(), User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
            jsonParse.getProjectData(sitesJson, i);

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_pages", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
            jsonParse.getSitePageData(sitesJson, i, "project");

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_perms", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
            jsonParse.getSitePermissions(sitesJson, i, "project");

            if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId()))
                sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_userPerms", User.getUserId() + File.separator + "memberships" + File.separator + SiteData.getProjects().get(i).getId());
            jsonParse.getUserSitePermissions(sitesJson, i, "project");
        }
    }
}
