package org.sakaiproject.api.customviews.calendar;

import java.util.ArrayList;

/**
 * Created by vasilis on 10/20/15.
 */
public class CalendarCollection {
    public String date = "";
    public String event_message = "";

    public static ArrayList<CalendarCollection> date_collection_arr = new ArrayList<>();

    public CalendarCollection(String date, String event_message) {

        this.date = date;
        this.event_message = event_message;

    }
}
