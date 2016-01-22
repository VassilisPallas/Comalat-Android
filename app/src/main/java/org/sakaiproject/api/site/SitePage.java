package org.sakaiproject.api.site;

import java.util.List;

/**
 * Created by vasilis on 1/15/16.
 * a JavaBeans convention class for the site page data
 */
public class SitePage {

    private int layout;
    private String toolPopupUrl;
    private boolean toolPopup;
    private String skin;
    private String id;
    private int position;
    private String title;
    private List<SiteTools> tools;

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getToolPopupUrl() {
        return toolPopupUrl;
    }

    public void setToolPopupUrl(String toolPopupUrl) {
        this.toolPopupUrl = toolPopupUrl;
    }

    public boolean isToolPopup() {
        return toolPopup;
    }

    public void setToolPopup(boolean toolPopup) {
        this.toolPopup = toolPopup;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SiteTools> getTools() {
        return tools;
    }

    public void setTools(List<SiteTools> tools) {
        this.tools = tools;
    }
}
