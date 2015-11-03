package org.sakaiproject.api.events;

import android.content.Context;
import android.util.Log;

import org.sakaiproject.api.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.UserEvents;
import org.sakaiproject.sakai.R;

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
    private static List<UserEvents> userEvents;
    private String userEventsJson;
    private String userEventInfoJson;
    private JsonParser jsonParse;
    private Context context;

    public OnlineEvents(Context context) {
        this.context = context;
        connection = Connection.getInstance();
        userEvents = new ArrayList<>();
        jsonParse = new JsonParser();
    }

    public static List<UserEvents> getUserEventsList() {
        return userEvents;
    }

    public void getUserEvents(String eventUrl) {
        try {

            connection.openConnection(eventUrl, ConnectionType.GET, true, false, null);
            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                userEventsJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseUserEventJson(userEventsJson);
                Actions.writeJsonFile(context, userEventsJson, "userEventsJson");


                for (int i = 0; i < userEvents.size(); i++) {
                    String owner = userEvents.get(i).getCreator();
                    String eventId = userEvents.get(i).getEventId();
                    String url;
                    if (owner.equals(User.getUserId())) {
                        url = context.getResources().getString(R.string.url) + "calendar/event/~" + owner + "/" + eventId + ".json";
                    } else {

                        // sometimes the status is 500 because of a unknown reason (says nullPointerException but the url is correct with the given creator's id)
                        // 500 - INTERNAL SERVER ERROR (general server failure, probably a failure in the provider)
                        do {
                            // get event's creator user id
                            connection.openConnection(context.getResources().getString(R.string.url) + "profile/" + userEvents.get(i).getCreator() + ".json", ConnectionType.GET, true, false, null);
                            status = connection.getResponseCode();
                            if (status >= 200 && status < 300) {
                                inputStream = new BufferedInputStream(connection.getInputStream());
                                String response = Actions.readJsonStream(inputStream);
                                jsonParse.getEventCreatorUserId(response, i);
                                inputStream.close();
                            }
                        } while (status == 500);

                        String siteId = userEvents.get(i).getReference().replaceAll("/calendar/calendar/", "");
                        siteId = siteId.replaceAll("/main", "");
                        userEvents.get(i).setSiteId(siteId);
                        url = context.getResources().getString(R.string.url) + "calendar/event/" + siteId + "/" + eventId + ".json";
                    }
                    connection.openConnection(url, ConnectionType.GET, true, false, null);
                    status = connection.getResponseCode();

                    if (status >= 200 && status < 300) {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        userEventInfoJson = Actions.readJsonStream(inputStream);
                        inputStream.close();
                        jsonParse.parseUserEventInfoJson(userEventInfoJson, i);
                        Actions.writeJsonFile(context, userEventInfoJson, "userEventInfoJson");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
    }
}
