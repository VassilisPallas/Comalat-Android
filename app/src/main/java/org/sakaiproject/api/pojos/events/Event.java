package org.sakaiproject.api.pojos.events;

import com.google.gson.annotations.SerializedName;

import org.sakaiproject.api.pojos.Time;

import java.util.List;

/**
 * Created by vspallas on 10/02/16.
 */
public class Event {

    @SerializedName("calendar_collection")
    private List<Items> calendarCollection;

    public List<Items> getCalendarCollection() {
        return calendarCollection;
    }

    public void setCalendarCollection(List<Items> calendarCollection) {
        this.calendarCollection = calendarCollection;
    }

    public static class Items {
        private String creator;
        private long duration;
        private String eventId;
        private Time firstTime;
        private String siteName;
        private String title;
        private String type;
        private String reference;

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public Time getFirstTime() {
            return firstTime;
        }

        public void setFirstTime(Time firstTime) {
            this.firstTime = firstTime;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
    }
}
