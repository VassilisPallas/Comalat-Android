package org.sakaiproject.api.pojos.wiki;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vspallas on 28/02/16.
 */
public class Wiki implements Serializable {
    private List<Object> childPages;
    private String name;
    private int numberOfComments;
    private String url;
    private String html;
    private List<Comment> comments;

    public List<Object> getChildPages() {
        return childPages;
    }

    public void setChildPages(List<Object> childPages) {
        this.childPages = childPages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
