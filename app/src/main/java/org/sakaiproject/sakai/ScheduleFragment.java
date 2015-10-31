package org.sakaiproject.sakai;


import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import org.sakaiproject.api.customviews.RecyclerItemClickListener;
import org.sakaiproject.api.customviews.adapters.SelectedDayEventsAdapter;
import org.sakaiproject.api.customviews.calendar.CalendarAdapter;
import org.sakaiproject.api.customviews.calendar.CalendarCollection;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.user.UserEvents;

import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    private OnlineEvents onlineEvents;
    private LinearLayout calendar;
    private ProgressBar progressBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

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
        cal_adapter = new CalendarAdapter(getContext(), cal_month, CalendarCollection.date_collection_arr);


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

                mAdapter = new SelectedDayEventsAdapter(getContext(), ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserEvents selectedEvent = OnlineEvents.getUserEventsList().get(position);

                FragmentManager fm = getFragmentManager();
                EventInfoFragment dialogFragment = new EventInfoFragment().setSelectedEvent(selectedEvent);
                dialogFragment.show(fm, "Event Info");

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        onlineEvents = new OnlineEvents(getContext());
        new EventsAsync().execute();

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                return true;
            default:
                break;
        }
        return false;
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

            if (NetWork.getConnectionEstablished()) {
                String url = getContext().getResources().getString(R.string.url) + "calendar/my.json";
                onlineEvents.getUserEvents(url);
            }

            return onlineEvents.getUserEventsList();
        }


        @Override
        protected void onPostExecute(List<UserEvents> userEvents) {
            super.onPostExecute(userEvents);

            cal_adapter.setEvents(userEvents);

            for (UserEvents event : userEvents) {
                CalendarCollection.date_collection_arr.add(new CalendarCollection(event.getEventWholeDate(), event.getTitle()));
            }

            calendar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}
