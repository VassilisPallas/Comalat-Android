package org.sakaiproject.sakai;


import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OfflineEvents;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.customviews.adapters.SelectedDayEventsAdapter;
import org.sakaiproject.customviews.adapters.CalendarAdapter;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.user.UserEvents;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {


    public static GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    private OfflineEvents offlineEvents;
    private LinearLayout calendar;
    private ProgressBar progressBar;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String selectedDate;

    public CalendarFragment() {
    }

    public static GregorianCalendar getCal_month() {
        return cal_month;
    }

    public static GregorianCalendar getCal_month_copy() {
        return cal_month_copy;
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public CalendarFragment setSelectedEvent(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
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


        GridView gridview = (GridView) v.findViewById(R.id.gv_calendar);
        //gridview.setExpanded(true);
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

                mAdapter = new SelectedDayEventsAdapter(getContext(), ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

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
                dialogFragment.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme );
                dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        offlineEvents = new OfflineEvents(getContext());
        new EventsAsync().execute();

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
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
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

    private class EventsAsync extends AsyncTask<Void, Void, List<UserEvents>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            calendar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<UserEvents> doInBackground(Void... params) {

            EventsCollection.getUserEventsList().clear();
            EventsCollection.getMonthEvents().clear();

            offlineEvents.getEvents();


            return EventsCollection.getUserEventsList();
        }


        @Override
        protected void onPostExecute(List<UserEvents> userEvents) {
            super.onPostExecute(userEvents);

            try {
                EventsCollection.findMonthlyEvents(cal_month_copy);
            } catch (ParseException | CloneNotSupportedException e) {
                e.printStackTrace();
            }

            cal_adapter.setEvents(EventsCollection.getMonthEvents());

            calendar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}