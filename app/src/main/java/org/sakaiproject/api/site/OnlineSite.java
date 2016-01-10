package org.sakaiproject.api.site;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vasilis on 11/21/15.
 */
public class OnlineSite {
    private Connection connection;
    private String json;
    private Context context;
    private InputStream inputStream;
    private JsonParser jsonParse;

    public OnlineSite(Context context) {
        this.context = context;
        this.jsonParse = new JsonParser(context);
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    public void getSites(String url) throws IOException {
        connection.openConnection(url, ConnectionType.GET, true, false, null);
        Integer status = connection.getResponseCode();
        if (status >= 200 && status < 300) {
            inputStream = new BufferedInputStream(connection.getInputStream());
            json = Actions.readJsonStream(inputStream);
            inputStream.close();
            jsonParse.parseSiteDataJson(json);
            Actions.writeJsonFile(context, json, "projectsAndSitesJson");

            for (int i = 0; i < SiteData.getSites().size(); i++) {
                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId(), ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.parseSiteWholeDataJson(json, i);
                    Actions.writeJsonFile(context, json, SiteData.getSites().get(i).getId());
                }
            }

            for (int i = 0; i < SiteData.getProjects().size(); i++) {
                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId(), ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.parseProjectWholeDataJson(json, i);
                    Actions.writeJsonFile(context, json, SiteData.getProjects().get(i).getId());
                }
            }
        }
    }
}
