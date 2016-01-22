package org.sakaiproject.api.site;

import android.content.Context;

import org.sakaiproject.general.Actions;
import org.sakaiproject.api.json.JsonParser;

import java.io.IOException;

/**
 * Created by vasilis on 1/11/16.
 */
public class OfflineSite {

    private Context context;
    private JsonParser jsonParse;

    /**
     * OfflineSite constructor
     * @param context the context
     */
    public OfflineSite(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
    }

    /**
     * parse the data from sites and projects the user has join
     * @throws IOException
     */
    public void getSites() throws IOException {
        String sitesJson = Actions.readJsonFile(context, "projectsAndSitesJson");
        jsonParse.parseSiteDataJson(sitesJson);

        for (int i = 0; i < SiteData.getSites().size(); i++) {
            sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId());
            jsonParse.getSiteData(sitesJson, i);

            sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_pages");
            jsonParse.getSitePageData(sitesJson, i, "site");

            sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_perms");
            jsonParse.getSitePermissions(sitesJson, i, "site");

            sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId() + "_userPerms");
            jsonParse.getUserSitePermissions(sitesJson, i, "site");
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId());
            jsonParse.getProjectData(sitesJson, i);

            sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_pages");
            jsonParse.getSitePageData(sitesJson, i, "project");

            sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_perms");
            jsonParse.getSitePermissions(sitesJson, i, "project");

            sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId() + "_userPerms");
            jsonParse.getUserSitePermissions(sitesJson, i, "project");
        }
    }
}
