package org.sakaiproject.api.events;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by vasilis on 11/5/15.
 * stores the user events
 */
public class EventsCollection {

    private static List<UserEvents> userEvents = new ArrayList<>();
    private static List<UserEvents> monthEvents = new ArrayList<>();

    public static List<UserEvents> getEventsList() {
        return userEvents;
    }

    public static List<UserEvents> getMonthEvents() {
        return monthEvents;
    }

    /**
     * get the monthly events based on the current month
     *
     * @param cal the calendar
     * @throws ParseException
     * @throws CloneNotSupportedException
     */
    public static void findMonthlyEvents(GregorianCalendar cal) throws ParseException, CloneNotSupportedException {
        monthEvents.clear();

        String month;

        month = String.valueOf(cal.get(Calendar.MONTH) + 1 /* starts from 0 */);
        month = month.length() > 1 ? month : "0" + month;

        selectedMonthEvents(month, cal);
    }

    /**
     * convert String to Date object
     *
     * @param dateStr the String which will be converted into Date
     * @return
     * @throws ParseException
     */
    public static Date readDateFromString(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.parse(dateStr);
    }

    /**
     * select the month events based on the current month
     *
     * @param month the current month on string index
     * @param cal   the calendar
     * @throws ParseException
     * @throws CloneNotSupportedException
     */
    public static void selectedMonthEvents(String month, GregorianCalendar cal) throws ParseException, CloneNotSupportedException {
        monthEvents.clear();
        for (UserEvents event : userEvents) {
            if (event.getMonth().equals(month)) {
                monthEvents.add(event);
            }
            if (event.getRecurrenceRule() != null) {
                if (event.getRecurrenceRule().getUntil() == null)
                    EventFrequency(event.getRecurrenceRule().getCount(), month, event.getEventWholeDate(), event.getRecurrenceRule().getInterval(), event.getRecurrenceRule().getFrequency(), cal, event);
                else
                    EventFrequency(event.getRecurrenceRule().getUntil().getMilliseconds(), month, event.getEventWholeDate(), event.getRecurrenceRule().getInterval(), event.getRecurrenceRule().getFrequency(), cal, event);
            }
        }
    }

    /**
     * get the last day of the month (31 for Jan etc)
     * it gets the date and goes to the next month,
     * then subtract - 1 day and finds the last day of
     * the previous month (the selected one)
     *
     * @param date the Date to check which day is the last one
     * @return the last day
     */
    public static int maxMonthDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();
        calendar.setTime(lastDayOfMonth);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * find the frequency fo the event without until date
     *
     * @param count        how many times the event will take place
     * @param currentMonth the month that is selected on the calendar
     * @param dateStr      the date of the event with the format yyyy-MM-dd
     * @param interval     the interval, eg. if the frequency is daily and the interval = 3,
     *                     then the event will take place every 3 days
     * @param frequency    the frequency of the event
     *                     <u>frequency types:</u>
     *                     day -> daily
     *                     week -> weekly
     *                     SMTW -> Sunday/Monday/Tuesday/Wednesday
     *                     SMW -> Sunday/Monday/Wednesday
     *                     STT -> Sunday/Tuesday/Thursday
     *                     MW -> Monday/Wednesday
     *                     MWF -> Monday/Wednesday/Friday
     *                     TTh -> Tuesday/Thursday
     *                     month -> monthly
     *                     year -> yearly
     * @param cal          the Gregorian calendar
     * @param event        the event's data
     * @throws ParseException
     * @throws CloneNotSupportedException
     */
    public static void EventFrequency(int count, String currentMonth, String dateStr, int interval, String frequency, Calendar cal, UserEvents event) throws ParseException, CloneNotSupportedException {
        int c = count == 0 ? 1 : 0;
        Date date;

        do {
            date = readDateFromString(dateStr);
            cal.setTime(date);

            int day = cal.get(cal.DAY_OF_WEEK);

            int dateDay = cal.get(cal.DAY_OF_MONTH);
            int dateMonth = cal.get(cal.MONTH) + 1 /* starts from 0 */;
            int dateYear = cal.get(cal.YEAR);

            switch (frequency) {
                case "MW":
                    if (day == cal.MONDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 5 + (7 * (interval - 1)); // 5 days until Monday *  -1 week (-1 because the first week has ended)
                    }
                    break;
                case "STT":
                    if (day == cal.SUNDAY || day == cal.TUESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 3 + (7 * (interval - 1)); // 3 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "SMW":
                    if (day == cal.SUNDAY) {
                        dateDay += 1;
                    } else if (day == cal.MONDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 4 + (7 * (interval - 1)); // 4 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "SMTW":
                    if (day == cal.SUNDAY || day == cal.MONDAY || day == cal.TUESDAY) {
                        dateDay += 1;
                    } else {
                        dateDay += 4 + (7 * (interval - 1)); // 4 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "MWF":
                    if (day == cal.MONDAY || day == cal.WEDNESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 3 + (7 * (interval - 1)); // 3 days until Monday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "TTh":
                    if (day == cal.TUESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 5 + (7 * (interval - 1)); // 5 days until tuesday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "day":
                    dateDay += interval;
                    break;
                case "week":
                    dateDay += 7 * interval;
                    break;
                case "month":
                    dateMonth += interval;
                    break;
                case "year":
                    dateYear += interval;
                    break;
            }

            int max = maxMonthDay(date);
            if (dateDay > max) {
                dateDay = dateDay - max;
                dateMonth++;
            }
            if (dateMonth > 12) {
                dateMonth = 1;
                dateYear++;
            }

            String monthStr = String.valueOf(dateMonth), dayStr = String.valueOf(dateDay);

            monthStr = monthStr.length() > 1 ? monthStr : "0" + monthStr;
            dayStr = dayStr.length() > 1 ? dayStr : "0" + dayStr;

            dateStr = dateYear + "-" + monthStr + "-" + dayStr;


            currentMonth = currentMonth.length() > 1 ? currentMonth : "0" + currentMonth;

            if (currentMonth.equals(monthStr)) {
                UserEvents e = (UserEvents) event.clone();
                e.setEventWholeDate(dateStr);
                monthEvents.add(e);
            }

            String nextMonth;
            if ((Integer.parseInt(currentMonth) + 1) > 12) {
                nextMonth = "01";
            } else {
                nextMonth = currentMonth.length() > 1 ? String.valueOf(Integer.parseInt(currentMonth) + 1) : "0" + Integer.parseInt(currentMonth + 1);
            }

            c++;

            if (nextMonth.equals(monthStr)) {
                break;
            }

        } while (c != count - 1);

    }

    /**
     * find the frequency fo the event without until date
     *
     * @param until        the last date until the event will take place
     * @param currentMonth the month that is selected on the calendar
     * @param dateStr      the date of the event with the format yyyy-MM-dd
     * @param interval     the interval, eg. if the frequency is daily and the interval = 3,
     *                     then the event will take place every 3 days
     * @param frequency    the frequency of the event
     *                     <u>frequency types:</u>
     *                     day -> daily
     *                     week -> weekly
     *                     SMTW -> Sunday/Monday/Tuesday/Wednesday
     *                     SMW -> Sunday/Monday/Wednesday
     *                     STT -> Sunday/Tuesday/Thursday
     *                     MW -> Monday/Wednesday
     *                     MWF -> Monday/Wednesday/Friday
     *                     TTh -> Tuesday/Thursday
     *                     month -> monthly
     *                     year -> yearly
     * @param cal          the Gregorian calendar
     * @param event        the event's data
     * @throws ParseException
     * @throws CloneNotSupportedException
     */
    public static void EventFrequency(long until, String currentMonth, String dateStr, int interval, String frequency, Calendar cal, UserEvents event) throws ParseException, CloneNotSupportedException {
        Date untilDate = new Date(until);
        Date date;
        do {

            date = readDateFromString(dateStr);
            cal.setTime(date);

            int day = cal.get(cal.DAY_OF_WEEK);

            int dateDay = cal.get(cal.DAY_OF_MONTH);
            int dateMonth = cal.get(cal.MONTH) + 1 /* starts from 0 */;
            int dateYear = cal.get(cal.YEAR);

            switch (frequency) {
                case "MW":
                    if (day == cal.MONDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 5 + (7 * (interval - 1)); // 5 days until Monday *  -1 week (-1 because the first week has ended)
                    }
                    break;
                case "STT":
                    if (day == cal.SUNDAY || day == cal.TUESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 3 + (7 * (interval - 1)); // 3 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "SMW":
                    if (day == cal.SUNDAY) {
                        dateDay += 1;
                    } else if (day == cal.MONDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 4 + (7 * (interval - 1)); // 4 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "SMTW":
                    if (day == cal.SUNDAY || day == cal.MONDAY || day == cal.TUESDAY) {
                        dateDay += 1;
                    } else {
                        dateDay += 4 + (7 * (interval - 1)); // 4 days until Sunday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "MWF":
                    if (day == cal.MONDAY || day == cal.WEDNESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 3 + (7 * (interval - 1)); // 3 days until Monday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "TTh":
                    if (day == cal.TUESDAY) {
                        dateDay += 2;
                    } else {
                        dateDay += 5 + (7 * (interval - 1)); // 5 days until tuesday * -1 week (-1 because the first week has ended)
                    }
                    break;
                case "day":
                    dateDay += interval;
                    break;
                case "week":
                    dateDay += 7 * interval;
                    break;
                case "month":
                    dateMonth += interval;
                    break;
                case "year":
                    dateYear += interval;
                    break;
            }

            int max = maxMonthDay(date);
            if (dateDay > max) {
                dateDay = dateDay - max;
                dateMonth++;
            }
            if (dateMonth > 12) {
                dateMonth = 1;
                dateYear++;
            }

            String monthStr = String.valueOf(dateMonth), dayStr = String.valueOf(dateDay);

            monthStr = monthStr.length() > 1 ? monthStr : "0" + monthStr;
            dayStr = dayStr.length() > 1 ? dayStr : "0" + dayStr;

            dateStr = dateYear + "-" + monthStr + "-" + dayStr;

            currentMonth = currentMonth.length() > 1 ? currentMonth : "0" + currentMonth;

            date = readDateFromString(dateStr);

            if (currentMonth.equals(monthStr) && date.before(untilDate)) {
                UserEvents e = (UserEvents) event.clone();
                e.setEventWholeDate(dateStr);
                monthEvents.add(e);
            }

            String nextMonth;
            if ((Integer.parseInt(currentMonth) + 1) > 12) {
                nextMonth = "01";
            } else {
                nextMonth = currentMonth.length() > 1 ? String.valueOf(Integer.parseInt(currentMonth) + 1) : "0" + Integer.parseInt(currentMonth + 1);
            }
            if (nextMonth.equals(monthStr)) {
                break;
            }

        } while (date.before(untilDate));
    }
}
