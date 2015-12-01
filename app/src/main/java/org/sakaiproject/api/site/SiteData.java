package org.sakaiproject.api.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasilis on 11/21/15.
 */
public class SiteData {

    private String id;
    private String type;
    private String title;

    private static List<SiteData> sites = new ArrayList<>();
    private static List<SiteData> projects = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public static List<SiteData> getSites() {
        return sites;
    }

    public static List<SiteData> getProjects() {
        return projects;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
