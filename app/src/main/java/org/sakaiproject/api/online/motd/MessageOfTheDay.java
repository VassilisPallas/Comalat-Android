package org.sakaiproject.api.online.motd;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sakaiproject.api.Actions;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.online.connection.ConnectionParams;

/**
 * Created by vasilis on 10/16/15.
 * Get the message of the day for the welcome screen
 */
public class MessageOfTheDay {

    private InputStream inputStream;
    private JsonParser jsonParse;
    private ConnectionParams connection;

    public MessageOfTheDay() {
        jsonParse = new JsonParser();
        connection = new ConnectionParams();
    }

    private List<String> message;
    private List<String> siteUrl;
    private MessageOfTheDay messageOfTheDay = this;

    public List<String> getMessage() {
        return message;
    }

    public List<String> getSiteUrl() {
        return siteUrl;
    }

    public MessageOfTheDay getMessageOfTheDayObj() {
        return messageOfTheDay;
    }

    public void setSiteUrl(List<String> siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void getMessageOfTheDay(String url) {
        try {
            connection.openConnection(url, "GET", true, null);

            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                String mothJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                messageOfTheDay = jsonParse.parseMotdJson(mothJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }
}
