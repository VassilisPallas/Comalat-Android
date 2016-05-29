package org.sakaiproject.api.memberships.pages.events;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.pojos.events.EventInfo;
import org.sakaiproject.api.pojos.UserEventOwner;
import org.sakaiproject.api.pojos.events.Event;
import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vspallas on 02/02/16.
 */
public class SiteEventsService {

    private Context context;
    private String siteId;
    private final Gson gson = new Gson();
    JsonObjectRequest ownerDataRequest;
    private Callback callback;
    private final String tag_user_events;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    /**
     * the SiteEventsService constructor
     *
     * @param context the context
     * @param siteId  site id
     */
    public SiteEventsService(Context context, String siteId, Callback callback) {
        this.context = context;
        this.siteId = siteId;
        this.callback = callback;
        tag_user_events = User.getUserEid() + " " + siteId + " events";
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * make REST call and get the json responses, then parse them for the data
     *
     * @param eventUrl the url
     */
    public void getEvents(String eventUrl) {

        EventsCollection.getEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest eventsRequest = new JsonObjectRequest(Request.Method.GET, eventUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Event event = gson.fromJson(response.toString(), Event.class);
                JsonParser.parseEventJson(event);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), "siteEventsJson", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                for (int i = 0; i < EventsCollection.getEventsList().size(); i++) {
                    String owner = EventsCollection.getEventsList().get(i).getCreator();
                    String eventId = EventsCollection.getEventsList().get(i).getEventId();
                    String url;
                    String tag = User.getUserEid() + " " + " " + siteId + " " + EventsCollection.getEventsList().get(i).getEventId() + " event";
                    if (!owner.equals(User.getUserId())) {
                        getOwnerData(context.getResources().getString(R.string.url) + "profile/" + EventsCollection.getEventsList().get(i).getCreator() + ".json", i, tag);
                    }

                    String siteId = EventsCollection.getEventsList().get(i).getReference().replaceAll("/calendar/calendar/", "");
                    siteId = siteId.replaceAll("/main", "");
                    EventsCollection.getEventsList().get(i).setSiteId(siteId);
                    url = context.getResources().getString(R.string.url) + "calendar/event/" + siteId + "/" + eventId + ".json";

                    getEventInfo(url, i, tag);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(eventsRequest, tag_user_events);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    private void getOwnerData(String url, final int index, final String tag) {
        ownerDataRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserEventOwner userEventOwner = gson.fromJson(response.toString(), UserEventOwner.class);
                JsonParser.getEventCreatorDisplayName(context, userEventOwner, index);
                Log.i("owner", userEventOwner.getDisplayName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppController.getInstance().addToRequestQueue(ownerDataRequest, tag);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(ownerDataRequest, tag);
    }

    private void getEventInfo(String url, final int index, final String tag) {

        JsonObjectRequest eventInfoRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EventInfo userEventOwnerPojo = gson.fromJson(response.toString(), EventInfo.class);
                JsonParser.parseEventInfoJson(userEventOwnerPojo, index);
                if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar"))
                    try {
                        ActionsHelper.writeJsonFile(context, response.toString(), EventsCollection.getEventsList().get(index).getEventId(), User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "calendar");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                if (index == EventsCollection.getEventsList().size() - 1) {
                    if (callback != null)
                        callback.onSuccess(null);
                    if (swipeRefreshLayout != null)
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("_validateSession", Connection.getSessionId());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(eventInfoRequest, tag);
    }
}
