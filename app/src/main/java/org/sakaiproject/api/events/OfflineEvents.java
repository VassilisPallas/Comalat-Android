package org.sakaiproject.api.events;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.sakaiproject.api.pojos.events.EventInfo;
import org.sakaiproject.api.pojos.events.Event;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 11/11/15.
 */
public class OfflineEvents {

    private String userEventsJson;
    private String userEventInfoJson;
    private Context context;
    private final Gson gson = new Gson();

    /**
     * the SiteOfflineEvents events constructor
     *
     * @param context the context
     */
    public OfflineEvents(Context context) {
        this.context = context;
    }

    /**
     * parsing the data to get the events for the offline mode
     */
    public void getEvents() {
        try {
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "events")) {
                userEventsJson = ActionsHelper.readJsonFile(context, "userEventsJson", User.getUserEid() + File.separator + "events");

                Event event = gson.fromJson(userEventsJson, Event.class);

                JsonParser.parseEventJson(event);
            }

            for (int i = 0; i < EventsCollection.getEventsList().size(); i++) {
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "events")) {
                    userEventInfoJson = ActionsHelper.readJsonFile(context, EventsCollection.getEventsList().get(i).getEventId(), User.getUserEid() + File.separator + "events");
                    EventInfo eventInfo = gson.fromJson(userEventInfoJson, EventInfo.class);
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
