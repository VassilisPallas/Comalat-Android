package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sakaiproject.adapters.SelectedDayEventsAdapter;
import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OfflineEvents;
import org.sakaiproject.api.events.UserEvents;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.events.SiteOfflineEvents;
import org.sakaiproject.api.sync.CalendarRefreshUI;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by vspallas on 23/02/16.
 */
public class EventTabFragment extends Fragment implements CalendarRefreshUI {

    private RecyclerView mRecyclerView;
    private SelectedDayEventsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noEvents;
    public static GregorianCalendar cal_month;
    private String siteName;
    private SiteData siteData;
    private List<UserEvents> monthlyEvents;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public EventTabFragment() {
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public EventTabFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        EventTabFragment eventTabFragment = new EventTabFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        eventTabFragment.setArguments(b);
        return eventTabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_events, container, false);

        swipeRefreshLayout = (CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.events_recycler_view);

        noEvents = (TextView) v.findViewById(R.id.no_events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // if the events recycle view is not on the top then the swipe refresh can not be done
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0 || recyclerView.getChildCount() == 1) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                List<UserEvents> todayEvents = new ArrayList<>();

                for (UserEvents ue : EventsCollection.getMonthEvents()) {
                    if (ue.getEventWholeDate().equals(monthlyEvents.get(position).getEventWholeDate())) {
                        todayEvents.add(ue);
                    }
                }

                UserEvents selectedEvent = todayEvents.get(position);

                FragmentManager fm = getFragmentManager();
                EventInfoFragment dialogFragment = new EventInfoFragment().setSelectedEvent(selectedEvent);
                dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
                dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        EventsCollection.getEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            OfflineEvents offlineEvents = new OfflineEvents(getContext());
            offlineEvents.getEvents();
        } else {
            SiteOfflineEvents siteOfflineEvents = new SiteOfflineEvents(getContext(), siteData.getId());
            siteOfflineEvents.getEvents();
        }

        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        monthlyEvents = EventsCollection.getMonthEvents();

        mAdapter = new SelectedDayEventsAdapter(getContext(), monthlyEvents);

        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter.getItemCount() == 0) {
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateUI() {

        EventsCollection.getEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            OfflineEvents offlineEvents = new OfflineEvents(getContext());
            offlineEvents.getEvents();
        } else {
            SiteOfflineEvents siteOfflineEvents = new SiteOfflineEvents(getContext(), siteData.getId());
            siteOfflineEvents.getEvents();
        }

        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        monthlyEvents = EventsCollection.getMonthEvents();

        mAdapter.setUserEventsList(monthlyEvents);

        if (mAdapter.getItemCount() == 0) {
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.GONE);
        }
    }
}
