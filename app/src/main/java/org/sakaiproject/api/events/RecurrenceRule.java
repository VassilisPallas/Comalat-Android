package org.sakaiproject.api.events;

import org.sakaiproject.api.time.Time;

import java.io.Serializable;

/**
 * Created by vasilis on 10/30/15.
 * Recurrence of the event
 */
public class RecurrenceRule implements Serializable {
    private int count;
    private String frequency;
    private String frequencyDescription;
    private int interval;
    private Time until;

    private String endDate;

    public RecurrenceRule() {
    }

    public RecurrenceRule(int count, String frequency, String frequencyDescription, int interval, Time until) {
        this.count = count;
        this.frequency = frequency;
        this.frequencyDescription = frequencyDescription;
        this.interval = interval;
        this.until = until;
    }

    public int getCount() {
        return count;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFrequencyDescription() {
        return frequencyDescription;
    }

    public int getInterval() {
        return interval;
    }

    public Time getUntil() {
        return until;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setFrequencyDescription(String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setUntil(Time until) {
        this.until = until;
    }

    public void setEndDate() {
        if (until != null) {
            String time = until.getDisplay();

            endDate = time.replaceAll(" \\d+:\\d+ (pm|am)", "");
        } else endDate = null;
    }
}

