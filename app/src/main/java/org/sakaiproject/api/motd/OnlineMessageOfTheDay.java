package org.sakaiproject.api.motd;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.general.Connection;

/**
 * Created by vasilis on 10/16/15.
 * Get the message of the day for the welcome screen
 */
public class OnlineMessageOfTheDay {

    private InputStream inputStream;
    private JsonParser jsonParse;
    private Connection connection;

    public OnlineMessageOfTheDay(Context context) {
        jsonParse = new JsonParser(context);
        connection = Connection.getInstance();
        connection.setContext(context);
    }

    private List<String> message;
    private List<String> siteUrl;
    private OnlineMessageOfTheDay onlineMessageOfTheDay = this;

    public List<String> getMessage() {
        return message;
    }

    public List<String> getSiteUrl() {
        return siteUrl;
    }

    public OnlineMessageOfTheDay getMessageOfTheDayObj() {
        return onlineMessageOfTheDay;
    }

    public void setSiteUrl(List<String> siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void getMessageOfTheDay(String url) {


        try {
            connection.openConnection(url, ConnectionType.GET, true, false, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                String mothJson = ActionsHelper.readJsonStream(inputStream);
                inputStream.close();
                onlineMessageOfTheDay = jsonParse.parseMotdJson(mothJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }
}
