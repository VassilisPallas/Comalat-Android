package org.sakaiproject.sakai;


import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OfflineEvents;
import org.sakaiproject.api.events.UserEventsService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.pages.events.SiteEventsService;
import org.sakaiproject.api.memberships.pages.events.SiteOfflineEvents;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.adapters.SelectedDayEventsAdapter;
import org.sakaiproject.adapters.CalendarAdapter;
import org.sakaiproject.api.events.UserEvents;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    public static GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    private OfflineEvents userOfflineEvents;
    private LinearLayout calendar;
    private ProgressBar progressBar;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrameLayout root;
    private GridView gridview;
    private TextView noEventsTextView;
    private ISwipeRefresh swipeRefresh;
    private String selectedDate;
    private SiteData siteData;
    private String siteName;
    private SiteOfflineEvents siteSiteOfflineEvents;
    private Callback callback = this;

    public CalendarFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        swipeRefresh = (ISwipeRefresh) context;
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public CalendarFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        CalendarFragment schedule = new CalendarFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        schedule.setArguments(b);
        return schedule;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        noEventsTextView = (TextView) v.findViewById(R.id.no_events_textview);

        root = (FrameLayout) v.findViewById(R.id.root);

        getActivity().setTitle(getContext().getResources().getString(R.string.calendar));

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        // the view for the event on the selected day
        mRecyclerView = (RecyclerView) v.findViewById(R.id.selected_day_events_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(getContext(), cal_month);

        tv_month = (TextView) v.findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        calendar = (LinearLayout) v.findViewById(R.id.calendar);

        progressBar = (ProgressBar) v.findViewById(R.id.schedule_progressbar);

        FrameLayout previous = (FrameLayout) v.findViewById(R.id.ib_prev);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        FrameLayout next = (FrameLayout) v.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });


        gridview = (GridView) v.findViewById(R.id.gv_calendar);
        gridview.setAdapter(cal_adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);

                selectedDate = selectedGridDate;

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    calendar.setVisibility(View.GONE);
                }


                if (((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate).size() == 0) {
                    noEventsTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    noEventsTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    mAdapter = new SelectedDayEventsAdapter(getContext(), ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

        });


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
                    if (ue.getEventWholeDate().equals(selectedDate)) {
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

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();
        Log.i("name", siteName);
        if (siteName.equals(getResources().getString(R.string.my_workspace)))
            userOfflineEvents = new OfflineEvents(getContext());
        else
            siteSiteOfflineEvents = new SiteOfflineEvents(getContext(), siteData.getId());

        new EventsAsync().execute();

        swipeRefresh.Callback(this);

        // Inflate the layout for this fragment
        return v;
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }


        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month_copy);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        mRecyclerView.setAdapter(null);

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }


        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month_copy);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        mRecyclerView.setAdapter(null);

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {

                    String url = null;
                    if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                        UserEventsService userEventsService = new UserEventsService(getContext(), callback);
                        userEventsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "calendar/my.json";
                        userEventsService.getEvents(url);
                    } else {
                        SiteEventsService siteSiteEventsService = new SiteEventsService(getContext(), siteData.getId(), callback);
                        siteSiteEventsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "calendar/site/" + siteData.getId() + ".json";
                        siteSiteEventsService.getEvents(url);
                    }

                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object obj) {
        try {
            EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month_copy);
        } catch (ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
        }

        cal_adapter.setEvents(EventsCollection.getMonthEvents());

        gridview.setAdapter(cal_adapter);
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private class EventsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            calendar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            EventsCollection.getEventsList().clear();
            EventsCollection.getMonthEvents().clear();

            if (siteName.equals(getResources().getString(R.string.my_workspace)))
                userOfflineEvents.getEvents();
            else
                siteSiteOfflineEvents.getEvents();

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);

            try {
                EventsCollection.selectedMonthEvents(String.valueOf(cal_month.get(cal_month.MONTH) + 1), cal_month_copy);
            } catch (ParseException | CloneNotSupportedException e) {
                e.printStackTrace();
            }

            mRecyclerView.setAdapter(null);

            cal_adapter.setEvents(EventsCollection.getMonthEvents());

            gridview.setAdapter(cal_adapter);

            calendar.setVisibility(View.VISIBLE);
            refreshCalendar();
            progressBar.setVisibility(View.GONE);

            //gridview.performItemClick(gridview.getChildAt(30), 30, gridview.getItemIdAtPosition(30));
        }
    }

}
