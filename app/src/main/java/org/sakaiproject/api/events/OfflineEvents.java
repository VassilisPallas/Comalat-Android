package org.sakaiproject.api.events;

import android.content.Context;
import android.content.SharedPreferences;

import org.sakaiproject.general.Actions;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 11/11/15.
 */
public class OfflineEvents {

    private JsonParser jsonParse;
    private String userEventsJson;
    private String userEventInfoJson;
    private Context context;

    /**
     * the OfflineEvents events constructor
     *
     * @param context the context
     */
    public OfflineEvents(Context context) {
        this.context = context;
        jsonParse = new JsonParser(context);
    }

    /**
     * parsing the data to get the events for the offline mode
     */
    public void getEvents() {
        try {
            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "events")) {
                userEventsJson = Actions.readJsonFile(context, "userEventsJson", User.getUserEid() + File.separator + "events");
                jsonParse.parseUserEventJson(userEventsJson);
            }

            for (int i = 0; i < EventsCollection.getUserEventsList().size(); i++) {
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "events")) {
                    userEventInfoJson = Actions.readJsonFile(context, EventsCollection.getUserEventsList().get(i).getEventId(), User.getUserEid() + File.separator + "events");
                    jsonParse.parseUserEventInfoJson(userEventInfoJson, i);
                }

                if (!EventsCollection.getUserEventsList().get(i).getCreator().equals(User.getUserId())) {
                    SharedPreferences prfs = context.getSharedPreferences("event_owners", Context.MODE_PRIVATE);
                    String owner = prfs.getString(EventsCollection.getUserEventsList().get(i).getEventId(), "");
                    EventsCollection.getUserEventsList().get(i).setCreatorUserId(owner);
                }
                String siteId = EventsCollection.getUserEventsList().get(i).getReference().replaceAll("/calendar/calendar/", "");
                siteId = siteId.replaceAll("/main", "");
                EventsCollection.getUserEventsList().get(i).setSiteId(siteId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
