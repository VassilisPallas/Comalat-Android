package org.sakaiproject.api.callback;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by vasilis on 11/3/15.
 */
public class Refresh extends AsyncTask<Void, Void, Void> {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Refresh constructor
     *
     * @param context the context
     */
    public Refresh(Context context) {
        this.context = context;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... params) {
//        EventsCollection.getEventsList().clear();
//        EventsCollection.getMonthEvents().clear();
//
//        UserEventsService userEventsService = new UserEventsService(context);
//        String url = context.getResources().getString(R.string.url) + "calendar/my.json";
//        userEventsService.getEvents(url);
//
//        try {
//            EventsCollection.selectedMonthEvents(String.valueOf(CalendarFragment.getCal_month().get(CalendarFragment.getCal_month().MONTH) + 1), CalendarFragment.getCal_month_copy());
//        } catch (NullPointerException e) {
//            // will be thrown if we update the data on any fragment except CalendarFragment
//        } catch (ParseException | CloneNotSupportedException e) {
//            e.printStackTrace();
//        }

//        SiteData.getSites().clear();
//        SiteData.getProjects().clear();
//        MembershipService membershipService = new MembershipService(context);
//        try {
//            membershipService.getSites(context.getString(R.string.url) + "membership.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //UserActivity.getSitesNavigationDrawer().fillSitesDrawer();
        swipeRefreshLayout.setRefreshing(false);
    }
}