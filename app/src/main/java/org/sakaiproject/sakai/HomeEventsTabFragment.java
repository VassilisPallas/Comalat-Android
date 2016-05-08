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
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.SelectedDayEventsAdapter;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OfflineEvents;
import org.sakaiproject.api.events.UserEvents;
import org.sakaiproject.api.events.UserEventsService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.events.SiteEventsService;
import org.sakaiproject.api.memberships.pages.events.SiteOfflineEvents;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by vassilis on 4/14/16.
 */
public class HomeEventsTabFragment extends Fragment implements Callback {

    private RecyclerView mRecyclerView;
    private SelectedDayEventsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noEvents;
    public static GregorianCalendar cal_month;
    private String siteName;
    private SiteData siteData;
    private List<UserEvents> monthlyEvents;

    public HomeEventsTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_home_events, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {
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

        EventsCollection.getEventsList().clear();
        EventsCollection.getMonthEvents().clear();

        fillList();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                UserEvents selectedEvent = EventsCollection.getMonthEvents().get(position);

                FragmentManager fm = getFragmentManager();
                EventInfoFragment dialogFragment = new EventInfoFragment().setSelectedEvent(selectedEvent);
                dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
                dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void fillList() {
        if (NetWork.getConnectionEstablished()) {
            String url;
            if (siteName.equals(getString(R.string.my_workspace))) {
                UserEventsService userEventsService = new UserEventsService(getContext(), this);
                url = getString(R.string.url) + "calendar/my.json";
                userEventsService.getEvents(url);
            } else {
                SiteEventsService siteSiteEventsService = new SiteEventsService(getContext(), siteData.getId(), this);
                url = getString(R.string.url) + "calendar/site/" + siteData.getId() + ".json";
                siteSiteEventsService.getEvents(url);
            }
        } else {
            if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                OfflineEvents offlineEvents = new OfflineEvents(getContext());
                offlineEvents.getEvents();
            } else {
                SiteOfflineEvents siteOfflineEvents = new SiteOfflineEvents(getContext(), siteData.getId());
                siteOfflineEvents.getEvents();
            }
            onSuccess(null);
        }
    }

    private String todayDate() {
        String day, month;
        int year;

        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        cal.setTime(d);
        year = cal.get(Calendar.YEAR);
        month = String.valueOf(cal.get(Calendar.MONTH) + 1 /* month starts from 0 */);
        day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        month = month.length() > 1 ? month : "0" + month;
        day = day.length() > 1 ? day : "0" + day;

        return year + "-" + month + "-" + day;
    }

    @Override
    public void onSuccess(Object obj) {
        EventsCollection.getMonthEvents().clear();
        List<UserEvents> todayEvents = new ArrayList<>();

        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        monthlyEvents = EventsCollection.getMonthEvents();

        for (UserEvents ue : monthlyEvents) {
            if (ue.getEventWholeDate().equals(todayDate())) {
                todayEvents.add(ue);
            }
        }

        mAdapter = new SelectedDayEventsAdapter(getContext(), todayEvents);

        if (mAdapter.getItemCount() == 0) {
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
    }
}
