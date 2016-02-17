package org.sakaiproject.api.memberships.pages.events;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.pojos.events.EventInfo;
import org.sakaiproject.api.pojos.events.Event;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 02/02/16.
 */
public class SiteOfflineEvents {

    private String siteEventsJson;
    private String siteEventInfoJson;
    private Context context;
    private String siteId;
    private final Gson gson = new Gson();

    /**
     * the SiteOfflineEvents events constructor
     *
     * @param context the context
     * @param siteId  site id
     */
    public SiteOfflineEvents(Context context, String siteId) {
        this.context = context;
        this.siteId = siteId;
    }

    /**
     * parsing the data to get the events for the offline mode
     */
    public void getEvents() {
        try {
            if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                siteEventsJson = Actions.readJsonFile(context, "siteEventsJson", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                Event event = gson.fromJson(siteEventsJson, Event.class);
                JsonParser.parseUserEventJson(event);
            }

            for (int i = 0; i < EventsCollection.getUserEventsList().size(); i++) {
                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                    siteEventInfoJson = Actions.readJsonFile(context, EventsCollection.getUserEventsList().get(i).getEventId(), User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                    EventInfo eventInfo = gson.fromJson(siteEventInfoJson, EventInfo.class);
                    JsonParser.parseUserEventInfoJson(eventInfo, i);
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
