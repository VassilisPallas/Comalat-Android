package org.sakaiproject.api.site;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by vasilis on 1/11/16.
 */
public class OfflineSite {

    private Context context;
    private JsonParser jsonParse;

    public OfflineSite(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
    }

    public void getSites() throws IOException {
        String sitesJson = Actions.readJsonFile(context, "projectsAndSitesJson");
        jsonParse.parseSiteDataJson(sitesJson);

        for (int i = 0; i < SiteData.getSites().size(); i++) {
            sitesJson = Actions.readJsonFile(context, SiteData.getSites().get(i).getId());
            jsonParse.parseSiteWholeDataJson(sitesJson, i);
        }

        for (int i = 0; i < SiteData.getProjects().size(); i++) {
            sitesJson = Actions.readJsonFile(context, SiteData.getProjects().get(i).getId());
            jsonParse.parseProjectWholeDataJson(sitesJson, i);
        }
    }
}
