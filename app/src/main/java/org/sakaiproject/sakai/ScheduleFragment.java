package org.sakaiproject.sakai;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.sakaiproject.api.customviews.calendar.CalendarAdapter;
import org.sakaiproject.api.customviews.calendar.CalendarCollection;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.user.data.UserEvents;

import java.io.IOException;
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

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);


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

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, ScheduleFragment.this);
            }

        });

        onlineEvents = new OnlineEvents(getContext());
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
                try {
                    onlineEvents.getUserEvents(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //offlineEvents.login(username, password);

            return onlineEvents.getUserEventsList();
        }


        @Override
        protected void onPostExecute(List<UserEvents> userEvents) {
            super.onPostExecute(userEvents);

            for (UserEvents event : userEvents) {
                CalendarCollection.date_collection_arr.add(new CalendarCollection(event.getEventTime(), event.getTitle()));
            }

            calendar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


}
