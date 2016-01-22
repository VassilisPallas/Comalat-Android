package org.sakaiproject.api.site;

import android.content.Context;

import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;
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

    /**
     * OnlineSite constructor
     * @param context the context
     */
    public OnlineSite(Context context) {
        this.context = context;
        this.jsonParse = new JsonParser(context);
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    /**
     * REST calls to get the data from sites and projects the user has join
     * @param url the url
     * @throws IOException
     */
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
                    jsonParse.getSiteData(json, i);
                    Actions.writeJsonFile(context, json, SiteData.getSites().get(i).getId());
                }

                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/pages", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getSitePageData(json, i, "site");
                    Actions.writeJsonFile(context, json, SiteData.getSites().get(i).getId() + "_pages");
                }


                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/perms", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getSitePermissions(json, i, "site");
                    Actions.writeJsonFile(context, json, SiteData.getSites().get(i).getId() + "_perms");
                }

                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getSites().get(i).getId() + "/userPerms", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getUserSitePermissions(json, i, "site");
                    Actions.writeJsonFile(context, json, SiteData.getSites().get(i).getId() + "_userPerms");
                }

            }

            for (int i = 0; i < SiteData.getProjects().size(); i++) {
                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId(), ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getProjectData(json, i);
                    Actions.writeJsonFile(context, json, SiteData.getProjects().get(i).getId());
                }

                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/pages", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getSitePageData(json, i, "project");
                    Actions.writeJsonFile(context, json, SiteData.getProjects().get(i).getId() + "_pages");
                }

                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/perms", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getSitePermissions(json, i, "project");
                    Actions.writeJsonFile(context, json, SiteData.getProjects().get(i).getId() + "_perms");
                }

                connection.openConnection(context.getResources().getString(R.string.url) + "site/" + SiteData.getProjects().get(i).getId() + "/userPerms", ConnectionType.GET, true, false, null);
                status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    json = Actions.readJsonStream(inputStream);
                    inputStream.close();
                    jsonParse.getUserSitePermissions(json, i, "project");
                    Actions.writeJsonFile(context, json, SiteData.getProjects().get(i).getId() + "_userPerms");
                }

            }
        }
    }
}
