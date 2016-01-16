package org.sakaiproject.api.events;

import android.content.Context;

import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.user.User;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vasilis on 10/20/15.
 * Get all the events
 */
public class OnlineEvents {
    private Connection connection;
    private InputStream inputStream;

    private String userEventsJson;
    private String userEventInfoJson;
    private JsonParser jsonParse;
    private Context context;

    public OnlineEvents(Context context) {
        this.context = context;
        connection = Connection.getInstance();
        connection.setContext(context);
        jsonParse = new JsonParser(context);
    }

    public void getEvents(String eventUrl) {
        try {

            connection.openConnection(eventUrl, ConnectionType.GET, true, false, null);
            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                userEventsJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseUserEventJson(userEventsJson);
                Actions.writeJsonFile(context, userEventsJson, "userEventsJson");


                for (int i = 0; i < EventsCollection.getUserEventsList().size(); i++) {
                    String owner = EventsCollection.getUserEventsList().get(i).getCreator();
                    String eventId = EventsCollection.getUserEventsList().get(i).getEventId();
                    String url;

                    if (!owner.equals(User.getUserId())) {
                        do {
                            // get event's creator user id
                            connection.openConnection(context.getResources().getString(R.string.url) + "profile/" + EventsCollection.getUserEventsList().get(i).getCreator() + ".json", ConnectionType.GET, true, false, null);
                            status = connection.getResponseCode();
                            if (status >= 200 && status < 300) {
                                inputStream = new BufferedInputStream(connection.getInputStream());
                                String response = Actions.readJsonStream(inputStream);
                                jsonParse.getEventCreatorDisplayName(response, i);
                                inputStream.close();
                            }
                        } while (status == 500);
                    }
                    String siteId = EventsCollection.getUserEventsList().get(i).getReference().replaceAll("/calendar/calendar/", "");
                    siteId = siteId.replaceAll("/main", "");
                    EventsCollection.getUserEventsList().get(i).setSiteId(siteId);
                    url = context.getResources().getString(R.string.url) + "calendar/event/" + siteId + "/" + eventId + ".json";

                    connection.openConnection(url, ConnectionType.GET, true, false, null);
                    status = connection.getResponseCode();

                    if (status >= 200 && status < 300) {
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        userEventInfoJson = Actions.readJsonStream(inputStream);
                        inputStream.close();
                        jsonParse.parseUserEventInfoJson(userEventInfoJson, i);
                        Actions.writeJsonFile(context, userEventInfoJson, EventsCollection.getUserEventsList().get(i).getEventId());
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
