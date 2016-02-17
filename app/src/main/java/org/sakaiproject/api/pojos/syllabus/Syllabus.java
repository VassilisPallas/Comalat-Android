package org.sakaiproject.api.pojos.syllabus;

import java.util.List;

/**
 * Created by vasilis on 1/28/16.
 * JavaBean conversion class for Syllabus tool
 */
public class Syllabus {

    private List<Item> items;
    private String redirectUrl;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
