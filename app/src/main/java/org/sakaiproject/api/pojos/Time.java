package org.sakaiproject.api.pojos;

import java.io.Serializable;

/**
 * Created by vasilis on 10/14/15.
 * This has created to get the data which are Time type from the response jsons
 */
public class Time implements Serializable {
    private String display;
    private Long time;

    public Time() {
    }

    public Time(String display, Long time) {
        this.display = display;
        this.time = time;
    }

    public String getDisplay() {
        return display;
    }

    public Long getTime() {
        return time;
    }

    public Long getMilliseconds() {
        return time;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
