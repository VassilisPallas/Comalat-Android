package org.sakaiproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.UserEvents;
import org.sakaiproject.sakai.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by vasilis on 10/20/15.
 */
public class CalendarAdapter extends BaseAdapter {

    private Context context;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    private List<UserEvents> userEvents;
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    private View previousView;

    private int todayPosition;

    /**
     * CalendarAdapter constructor
     *
     * @param context       the context
     * @param monthCalendar the month Calendar
     */
    public CalendarAdapter(Context context, GregorianCalendar monthCalendar) {
        CalendarAdapter.day_string = new ArrayList<>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.items = new ArrayList<>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());

        refreshDays();

    }

    public int getTodayPosition() {
        return todayPosition;
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public void dateColor(TextView dayView, int position, String gridvalue) {
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting current month's days in white color.
            dayView.setTextColor(Color.WHITE);
        }
    }


    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);

        }


        dayView = (TextView) v.findViewById(R.id.date);

        String[] separatedTime = day_string.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        dateColor(dayView, position, gridvalue);

        if (day_string.get(position).equals(curentDateString)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
            } else {
                v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }
            todayPosition = position;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                v.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month, context.getTheme()));
            } else {
                v.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month));
            }
        }

        dayView.setText(gridvalue);

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        /*ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.GONE);
        }
        */

        setEventView(v, position, dayView);

        return v;
    }

    public View setSelected(View view, int pos) {
        if (previousView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                previousView.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month, context.getTheme()));
            } else {
                previousView.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        int len = day_string.size();
        if (len > pos) {
            if (day_string.get(pos).equals(curentDateString)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
                } else {
                    view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }

                todayPosition = pos;
            } else {
                previousView = view;
            }
        }


        return view;
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }


    public void setEventView(View v, int pos, TextView txt) {

        int len = EventsCollection.getMonthEvents().size();

        for (int i = 0; i < len; i++) {
            UserEvents event = EventsCollection.getMonthEvents().get(i);
            String date = event.getEventWholeDate();
            int len1 = day_string.size();
            if (len1 > pos) {

                if (day_string.get(pos).equals(date) && !day_string.get(pos).equals(curentDateString)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month, context.getTheme()));
                    } else {
                        v.setBackgroundColor(context.getResources().getColor(R.color.calendar_previous_month));
                    }
                    SpannableString spanString = new SpannableString(txt.getText());
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                    spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);


                    dateColor(txt, pos, txt.getText().toString());

                    txt.setText(spanString);
                } else if (day_string.get(pos).equals(date) && day_string.get(pos).equals(curentDateString)) {
                    todayPosition = pos;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
                    } else {
                        v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    }
                    SpannableString spanString = new SpannableString(txt.getText());
                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                    spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);


                    dateColor(txt, pos, txt.getText().toString());

                    txt.setText(spanString);
                }
            }
        }

    }

    public void setEvents(List<UserEvents> userEvents) {
        this.userEvents = userEvents;
    }


    public List<UserEvents> getPositionList(String date) {

        List<UserEvents> todayEvents = new ArrayList<>();

        int len = userEvents.size();
        for (int i = 0; i < len; i++) {
            UserEvents event = userEvents.get(i);
            if (event.getEventWholeDate().equals(date)) {
                todayEvents.add(event);
            }
        }

        return todayEvents;
    }
}
