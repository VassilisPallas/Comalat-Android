package org.sakaiproject.api.pages.syllabus;

import android.content.Context;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vasilis on 1/28/16.
 */
public class OnlineSyllabus {

    private Connection connection;
    private String json;
    private Context context;
    private InputStream inputStream;
    private String siteId;
    private JsonParser jsonParse;

    /**
     * OnlineSyllabus constructor
     *
     * @param context the context
     */
    public OnlineSyllabus(Context context, String siteId) {
        this.context = context;
        this.siteId = siteId;
        connection = Connection.getInstance();
        connection.setContext(context);
        jsonParse = new JsonParser(context);
    }

    public void getSyllabus(String url) throws IOException {
        connection.openConnection(url, ConnectionType.GET, true, false, null);
        Integer status = connection.getResponseCode();
        if (status >= 200 && status < 300) {
            inputStream = new BufferedInputStream(connection.getInputStream());
            json = Actions.readJsonStream(inputStream);
            inputStream.close();
            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
                Actions.writeJsonFile(context, json, siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");
        }
    }

}
