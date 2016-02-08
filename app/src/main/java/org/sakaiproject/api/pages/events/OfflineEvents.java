package org.sakaiproject.api.pages.events;

import android.content.Context;
import android.content.SharedPreferences;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 02/02/16.
 */
public class OfflineEvents {

    private JsonParser jsonParse;
    private String siteEventsJson;
    private String userEventInfoJson;
    private Context context;
    private String siteId;

    /**
     * the OfflineEvents events constructor
     *
     * @param context the context
     * @param siteId  site id
     */
    public OfflineEvents(Context context, String siteId) {
        this.context = context;
        this.siteId = siteId;
        jsonParse = new JsonParser(context);
    }

    /**
     * parsing the data to get the events for the offline mode
     */
    public void getEvents() {
        try {
            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                siteEventsJson = Actions.readJsonFile(context, "siteEventsJson", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                jsonParse.parseUserEventJson(siteEventsJson);
            }

            for (int i = 0; i < EventsCollection.getUserEventsList().size(); i++) {
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                    userEventInfoJson = Actions.readJsonFile(context, EventsCollection.getUserEventsList().get(i).getEventId(), User.getUserEid() + File.separator + "memberships" + File.separator + EventsCollection.getUserEventsList().get(i).getSiteId() + File.separator + "calendar");
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
