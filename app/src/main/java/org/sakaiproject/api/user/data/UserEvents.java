package org.sakaiproject.api.user.data;

import org.sakaiproject.api.time.Time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 10/20/15.
 */
public class UserEvents {
    private String creator;
    private long duration;
    private String eventId;
    private Time firstTime;
    //private RecurrenceRule recurrenceRule;
    private String reference;
    private String siteName;
    private String title;
    private String type;
    private String entityReference;
    private String entityURL;
    private String entityTitle;

    private String eventTime;

    public UserEvents() {
    }

    public String getCreator() {
        return creator;
    }

    public long getDuration() {
        return duration;
    }

    public String getEventId() {
        return eventId;
    }

    public Time getFirstTime() {
        return firstTime;
    }

    public String getReference() {
        return reference;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setFirstTime(Time firstTime) {
        this.firstTime = firstTime;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public void setEntityURL(String entityURL) {
        this.entityURL = entityURL;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }

    public void setEventTime() {

        String day = "", month = "", year = "";

        String wholeTime = firstTime.getDisplay();
        String split[] = wholeTime.split(",");

        // Delete hour eg 15:00 am
        String strForYear = split[1].replaceAll("(\\d+:\\d+ (am|pm))+", " ");

        Pattern dayPattern = Pattern.compile("\\d+");
        Pattern monthPattern = Pattern.compile("[a-zA-z]+");
        Pattern yearPattern = Pattern.compile("\\d+");

        Matcher dayMatcher = dayPattern.matcher(split[0]);
        Matcher monthMatcher = monthPattern.matcher(split[0]);
        Matcher yearMatcher = yearPattern.matcher(strForYear);

        while (dayMatcher.find()) {
            day = dayMatcher.group().trim();
        }

        while (monthMatcher.find()) {
            month = monthMatcher.group().trim();
        }

        while (yearMatcher.find()) {
            year = yearMatcher.group().trim();
        }

        String monthNum = matchMonth(month);

        day = day.length() > 1 ? day : "0" + day;

        eventTime = year + "-" + monthNum + "-" + day;
    }

    public String matchMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return null;
        }
    }
}
