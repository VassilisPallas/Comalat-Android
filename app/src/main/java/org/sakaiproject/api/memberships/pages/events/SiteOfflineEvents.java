package org.sakaiproject.api.memberships.pages.events;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.pojos.events.EventInfo;
import org.sakaiproject.api.pojos.events.Event;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

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
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                siteEventsJson = ActionsHelper.readJsonFile(context, "siteEventsJson", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                Event event = gson.fromJson(siteEventsJson, Event.class);
                JsonParser.parseEventJson(event);
            }

            for (int i = 0; i < EventsCollection.getEventsList().size(); i++) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar")) {
                    siteEventInfoJson = ActionsHelper.readJsonFile(context, EventsCollection.getEventsList().get(i).getEventId(), User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                    EventInfo eventInfo = gson.fromJson(siteEventInfoJson, EventInfo.class);
                    JsonParser.parseEventInfoJson(eventInfo, i);
                }

                if (!EventsCollection.getEventsList().get(i).getCreator().equals(User.getUserId())) {
                    SharedPreferences prfs = context.getSharedPreferences("event_owners", Context.MODE_PRIVATE);
                    String owner = prfs.getString(EventsCollection.getEventsList().get(i).getEventId(), "");
                    EventsCollection.getEventsList().get(i).setCreatorUserId(owner);
                }
                String siteId = EventsCollection.getEventsList().get(i).getReference().replaceAll("/calendar/calendar/", "");
                siteId = siteId.replaceAll("/main", "");
                EventsCollection.getEventsList().get(i).setSiteId(siteId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
