package org.sakaiproject.api.pojos.events;

import org.sakaiproject.api.pojos.Attachment;
import org.sakaiproject.api.pojos.Time;

import java.util.List;

/**
 * Created by vspallas on 10/02/16.
 */
public class EventInfo {
    private String description;
    private Time lastTime;
    private String location;
    private RecurrenceRule recurrenceRule;
    private List<Attachment> attachments;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time getLastTime() {
        return lastTime;
    }

    public void setLastTime(Time lastTime) {
        this.lastTime = lastTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public RecurrenceRule getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(RecurrenceRule recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
