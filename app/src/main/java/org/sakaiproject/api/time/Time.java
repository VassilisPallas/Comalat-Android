package org.sakaiproject.api.time;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vasilis on 10/14/15.
 */
public class Time implements Serializable {
    private String display;
    private Date time;

    public Time() {
    }

    public Time(String display, Date time) {
        this.display = display;
        this.time = time;
    }

    public String getDisplay() {
        return display;
    }

    public Date getTime() {
        return time;
    }

    public long getMilliseconds() {
        return time.getTime();
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
