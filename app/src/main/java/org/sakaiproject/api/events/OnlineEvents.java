package org.sakaiproject.api.events;

import android.content.Context;

import org.sakaiproject.api.customviews.calendar.CalendarCollection;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.server.Actions;
import org.sakaiproject.api.server.Connection;
import org.sakaiproject.api.user.data.UserEvents;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 10/20/15.
 * Get all the events
 */
public class OnlineEvents {
    private Connection connection;
    private InputStream inputStream;
    private List<UserEvents> userEvents;
    private String userEventsJson;
    private JsonParser jsonParse;
    private Context context;

    public OnlineEvents(Context context) {
        this.context = context;
        connection = new Connection();
        userEvents = new ArrayList<>();
        jsonParse = new JsonParser();
    }

    public List<UserEvents> getUserEventsList() {
        return userEvents;
    }

    public void getUserEvents(String url) throws IOException {
        connection.openConnection(url, "GET", true, null);
        Integer status = connection.getResponseCode();
        if (status >= 200 && status < 300) {
            inputStream = new BufferedInputStream(connection.getInputStream());
            userEventsJson = Actions.readJsonStream(inputStream);
            inputStream.close();
            userEvents = jsonParse.parseUserEventJson(userEventsJson);
            Actions.writeJsonFile(context, userEventsJson, "userEventsJson");
        }
    }
}
