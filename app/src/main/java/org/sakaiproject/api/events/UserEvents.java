package org.sakaiproject.api.events;

import org.sakaiproject.api.pojos.Attachment;
import org.sakaiproject.api.pojos.Time;
import org.sakaiproject.api.pojos.events.RecurrenceRule;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vasilis on 10/20/15.
 * * a JavaBeans convention class for the user events data
 */
public class UserEvents implements Serializable, Cloneable {

    private String creator;
    private long duration;
    private String eventId;
    private Time firstTime;
    private RecurrenceRule recurrenceRule;
    private String siteName;
    private String title;
    private String type;
    private List<Attachment> attachments = new ArrayList<>();
    private String description;

    private Time lastTime;

    private String location;

    private String eventWholeDate;

    private String eventDate;

    private String timeDuration;

    private String reference;

    private String siteId;

    private String creatorUserId;

    private String month;

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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<String> getAttachmentNames() {

        List<String> names = new ArrayList<>();

        for (int i = 0; i < attachments.size(); i++) {

            String name = attachments.get(i).getUrl().substring(attachments.get(i).getUrl().lastIndexOf('/') + 1).toLowerCase();

            if (name.startsWith("http")) {
                name = name.replaceAll("%3A|%3a", ":");
                name = name.replaceAll("_", "/");
                //name = "click to open url";
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

    public void setAttachments(List<Attachment> attachments) {
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

    public String getMonth() {
        Calendar cal = Calendar.getInstance();
        Date d = new Date(firstTime.getMilliseconds());
        cal.setTime(d);

        return String.valueOf(cal.get(cal.MONTH) + 1);
    }

    public void setEventWholeDate() {

        String day, month;
        int year;

        Calendar cal = Calendar.getInstance();
        Date d = new Date(firstTime.getMilliseconds());
        cal.setTime(d);
        year = cal.get(Calendar.YEAR);
        month = String.valueOf(cal.get(Calendar.MONTH) + 1 /* month starts from 0 */);
        day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        month = month.length() > 1 ? month : "0" + month;
        day = day.length() > 1 ? day : "0" + day;

        eventWholeDate = year + "-" + month + "-" + day;
    }

    public void setEventWholeDate(String eventWholeDate) {
        this.eventWholeDate = eventWholeDate;
    }

    public void setEventDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        eventDate = dateFormat.format(firstTime.getTime());
    }

    public void setTimeDuration() {

        Date start = new Date(firstTime.getTime());
        Date end = new Date(lastTime.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);

        timeDuration = dateFormat.format(start).toLowerCase() + " - " + dateFormat.format(end).toLowerCase();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
