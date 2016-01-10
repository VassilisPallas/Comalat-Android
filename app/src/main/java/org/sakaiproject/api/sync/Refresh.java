package org.sakaiproject.api.sync;

import android.content.Context;
import android.os.AsyncTask;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.site.OnlineSite;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.sakai.CalendarFragment;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by vasilis on 11/3/15.
 */
public class Refresh extends AsyncTask<Void, Void, Void> {

    private Context context;
    private CustomSwipeRefreshLayout swipeRefreshLayout;

    public Refresh(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... params) {
        EventsCollection.getUserEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        OnlineEvents onlineEvents = new OnlineEvents(context);
        String url = context.getResources().getString(R.string.url) + "calendar/my.json";
        onlineEvents.getEvents(url);

        try {
            EventsCollection.selectedMonthEvents(String.valueOf(CalendarFragment.getCal_month().get(CalendarFragment.getCal_month().MONTH) + 1), CalendarFragment.getCal_month_copy());
        } catch (NullPointerException e) {
            // will be thrown if we update the data on any fragment except CalendarFragment
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        SiteData.getSites().clear();
        SiteData.getProjects().clear();
        OnlineSite onlineSite = new OnlineSite(context);
        try {
            onlineSite.getSites(context.getString(R.string.url) + "membership.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Actions.fillSitesDrawer(UserActivity.getSiteMenu());
        swipeRefreshLayout.setRefreshing(false);
    }
}