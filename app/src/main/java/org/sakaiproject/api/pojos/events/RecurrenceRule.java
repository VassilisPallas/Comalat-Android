package org.sakaiproject.api.pojos.events;

import org.sakaiproject.api.pojos.Time;

import java.io.Serializable;

/**
 * Created by vasilis on 10/30/15.
 * a JavaBeans convention class for the Recurrence of the event
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

    /**
     * the RecurrenceRule constructor
     *
     * @param count                the times that the event will take place (eg 15 times)
     * @param frequency            the event frequency (eg yearly, monthly etc)
     * @param frequencyDescription the frequency description (eg Monday, Tuesday etc)
     * @param interval             the event interval (eg per 2 days)
     * @param until                the date until the event will take place
     */
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

