package org.sakaiproject.api.pages.syllabus;

import java.util.Date;
import java.util.List;

/**
 * Created by vasilis on 1/28/16.
 */
public class Item {
    private List<String> attachments;
    private String data;
    private long endDate;
    private long startDate;
    private int order;
    private String title;

    public Item(List<String> attachments, String data, long endDate, long startDate, int order, String title) {
        this.attachments = attachments;
        this.data = data;
        this.endDate = endDate;
        this.startDate = startDate;
        this.order = order;
        this.title = title;
    }

    public Item() {
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
