package org.sakaiproject.api.user;

import org.sakaiproject.api.time.Time;
import org.sakaiproject.api.events.RecurrenceRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 10/20/15.
 */
public class UserEvents implements Serializable {

    private String creator;
    private long duration;
    private String eventId;
    private Time firstTime;
    private RecurrenceRule recurrenceRule;
    private String siteName;
    private String title;
    private String type;
    private List<String> attachments = new ArrayList<>();
    private String description;

    private Time lastTime;

    private String location;

    private String eventWholeDate;

    private String eventDate;

    private String timeDuration;

    private String reference;

    private String siteId;

    private String creatorUserId;

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

    public String getSiteName() {
        return siteName;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Time getLastTime() {
        return lastTime;
    }

    public String getLocation() {
        return location;
    }

    public String getEventWholeDate() {
        return eventWholeDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public RecurrenceRule getRecurrenceRule() {
        return recurrenceRule;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public List<String> getAttachmentNames() {

        List<String> names = new ArrayList<>();

        for (int i = 0; i < attachments.size(); i++) {

            String name = attachments.get(i).substring(attachments.get(i).lastIndexOf('/') + 1).toLowerCase();

            if (name.startsWith("http")) {
//                name = name.replaceAll("%3A|%3a", ":");
//                name = name.replaceAll("_", "/");
                name = "click to open url";
            }

            names.add(name);
        }
        return names;
    }

    public String getReference() {
        return reference;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public void setRecurrenceRule(RecurrenceRule recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
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

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastTime(Time lastTime) {
        this.lastTime = lastTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEventWholeDate() {

        String day = "", month = "", year = "";

        String wholeTime = firstTime.getDisplay();
        String split[] = wholeTime.split(",");

        // Delete hour eg 15:00 am
        String strForYear = split[1].replaceAll("(\\d+:\\d+ (am|pm))+", "");

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

        eventWholeDate = year + "-" + monthNum + "-" + day;
    }

    public void setEventDate() {
        String wholeTime = firstTime.getDisplay();
        eventDate = wholeTime.replaceAll("( \\d+:\\d+ (am|pm))+", "");
    }

    public void setTimeDuration() {
        String start = firstTime.getDisplay();
        String end = lastTime.getDisplay();
        String firstSplit[] = start.split(",");
        String lastSplit[] = end.split(",");

        start = firstSplit[1].replaceAll(" (\\d{4}) ", "");
        end = lastSplit[1].replaceAll(" (\\d{4}) ", "");

        timeDuration = start + " - " + end;
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
