package org.sakaiproject.api.pages.events;

import android.content.Context;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vspallas on 02/02/16.
 */
public class OnlineEvents {
    private Connection connection;
    private InputStream inputStream;

    private String siteEventsJson;
    private String siteEventInfoJson;
    private JsonParser jsonParse;
    private Context context;
    private String siteId;

    /**
     * the OnlineEvents constructor
     *
     * @param context the context
     * @param siteId  site id
     */
    public OnlineEvents(Context context, String siteId) {
        this.context = context;
        this.siteId = siteId;
        connection = Connection.getInstance();
        connection.setContext(context);
        jsonParse = new JsonParser(context);
    }

    /**
     * make REST call and get the json responses, then parse them for the data
     *
     * @param eventUrl the url
     */
    public void getEvents(String eventUrl) {
        try {

            connection.openConnection(eventUrl, ConnectionType.GET, true, false, null);
            Integer status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                siteEventsJson = Actions.readJsonStream(inputStream);
                inputStream.close();
                jsonParse.parseUserEventJson(siteEventsJson);

                if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar"))
                    Actions.writeJsonFile(context, siteEventsJson, "siteEventsJson", User.getUserId() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");

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
                        siteEventInfoJson = Actions.readJsonStream(inputStream);
                        inputStream.close();
                        jsonParse.parseUserEventInfoJson(siteEventInfoJson, i);
                        if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar"))
                            Actions.writeJsonFile(context, siteEventInfoJson, EventsCollection.getUserEventsList().get(i).getEventId(), User.getUserId() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
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
