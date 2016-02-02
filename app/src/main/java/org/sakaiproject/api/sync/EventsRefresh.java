package org.sakaiproject.api.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.customviews.adapters.CalendarAdapter;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.CalendarFragment;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 1/24/16.
 */
public class EventsRefresh extends AsyncTask<Void, Void, Void> {

    private Context context;
    private GridView gridView;
    private CalendarAdapter adapter;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private String siteName;
    private org.sakaiproject.api.pages.events.OnlineEvents siteOnlineEvents;

    public EventsRefresh(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    public void setAdapter(CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setSiteData(SiteData siteData) {
        this.siteData = siteData;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override
    protected Void doInBackground(Void... params) {
        EventsCollection.getUserEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        OnlineEvents onlineEvents = new OnlineEvents(context);
        String url = null;
        if (siteName.equals(context.getResources().getString(R.string.my_workspace))) {
            url = context.getResources().getString(R.string.url) + "calendar/my.json";
            onlineEvents.getEvents(url);
        } else {
            siteOnlineEvents = new org.sakaiproject.api.pages.events.OnlineEvents(context, siteData.getId());
            url = context.getResources().getString(R.string.url) + "calendar/site/" + siteData.getId() + ".json";
            siteOnlineEvents.getEvents(url);
        }

        try {
            EventsCollection.selectedMonthEvents(String.valueOf(CalendarFragment.getCal_month().get(CalendarFragment.getCal_month().MONTH) + 1), CalendarFragment.getCal_month_copy());
        } catch (NullPointerException e) {
            // will be thrown if we update the data on any fragment except CalendarFragment
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        try {
            EventsCollection.findMonthlyEvents(CalendarFragment.getCal_month_copy());
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }


        adapter.setEvents(EventsCollection.getMonthEvents());

        gridView.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

}
