package org.sakaiproject.api.pojos.membership;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by vasilis on 1/15/16.
 * a JavaBeans convention class for the site page data
 */
public class SitePage implements Serializable {

    private int layout;
    private String toolPopupUrl;
    private boolean toolPopup;
    private String skin;
    private String id;
    private int position;
    private String title;
    private List<SiteTools> tools;
    private Map<String, String> props;
    private boolean titleCustom, activeEdit, homePage, popup;

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

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public boolean isTitleCustom() {
        return titleCustom;
    }

    public void setTitleCustom(boolean titleCustom) {
        this.titleCustom = titleCustom;
    }

    public boolean isActiveEdit() {
        return activeEdit;
    }

    public void setActiveEdit(boolean activeEdit) {
        this.activeEdit = activeEdit;
    }

    public boolean isHomePage() {
        return homePage;
    }

    public void setHomePage(boolean homePage) {
        this.homePage = homePage;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }
}
