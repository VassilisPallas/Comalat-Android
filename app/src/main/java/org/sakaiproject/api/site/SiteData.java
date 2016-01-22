package org.sakaiproject.api.site;

import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;

import org.sakaiproject.api.time.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by vasilis on 11/21/15.
 * a JavaBeans convention class for the site data
 */
public class SiteData {

    private String id;
    private String type;
    private String title;
    private String memberRole;
    private boolean active, provided;
    private String contactEmail;
    private String contactName;
    private Time createdTime;
    private String description;
    private String shortDescription;
    private String iconUrlFull;
    private String infoUrlFull;
    private String joinerRole;
    private long lastModified;
    private String maintainRole;
    private Time modifiedTime;
    private String owner;
    private Map<String, String> props;
    private String providerGroupId;
    private List<String> siteGroups;
    private Owner siteOwner;
    private String skin;
    private Date softlyDeletedDate;
    private List<String> userRoles;
    private boolean activeEdit;
    private boolean customPageOrdered;
    private boolean empty;
    private boolean joinable;
    private boolean pubView;
    private boolean published;
    private boolean softlyDeleted;

    private List<SitePage> pages;

    private List<String> access, maintain;
    private List<String> userSitePermissions;

    private static List<SiteData> sites = new ArrayList<>();
    private static List<SiteData> projects = new ArrayList<>();


    public static List<SiteData> getSites() {
        return sites;
    }

    public static List<SiteData> getProjects() {
        return projects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isProvided() {
        return provided;
    }

    public void setProvided(boolean provided) {
        this.provided = provided;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIconUrlFull() {
        return iconUrlFull;
    }

    public void setIconUrlFull(String iconUrlFull) {
        this.iconUrlFull = iconUrlFull;
    }

    public String getInfoUrlFull() {
        return infoUrlFull;
    }

    public void setInfoUrlFull(String infoUrlFull) {
        this.infoUrlFull = infoUrlFull;
    }

    public String getJoinerRole() {
        return joinerRole;
    }

    public void setJoinerRole(String joinerRole) {
        this.joinerRole = joinerRole;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getMaintainRole() {
        return maintainRole;
    }

    public void setMaintainRole(String maintainRole) {
        this.maintainRole = maintainRole;
    }

    public Time getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Time modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public List<String> getSiteGroups() {
        return siteGroups;
    }

    public void setSiteGroups(List<String> siteGroups) {
        this.siteGroups = siteGroups;
    }

    public Owner getSiteOwner() {
        return siteOwner;
    }

    public void setSiteOwner(Owner siteOwner) {
        this.siteOwner = siteOwner;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Date getSoftlyDeletedDate() {
        return softlyDeletedDate;
    }

    public void setSoftlyDeletedDate(Date softlyDeletedDate) {
        this.softlyDeletedDate = softlyDeletedDate;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public boolean isActiveEdit() {
        return activeEdit;
    }

    public void setActiveEdit(boolean activeEdit) {
        this.activeEdit = activeEdit;
    }

    public boolean isCustomPageOrdered() {
        return customPageOrdered;
    }

    public void setCustomPageOrdered(boolean customPageOrdered) {
        this.customPageOrdered = customPageOrdered;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public boolean isPubView() {
        return pubView;
    }

    public void setPubView(boolean pubView) {
        this.pubView = pubView;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isSoftlyDeleted() {
        return softlyDeleted;
    }

    public void setSoftlyDeleted(boolean softlyDeleted) {
        this.softlyDeleted = softlyDeleted;
    }

    public List<SitePage> getPages() {
        return pages;
    }

    public void setPages(List<SitePage> pages) {
        this.pages = pages;
    }

    public List<String> getAccess() {
        return access;
    }

    public void setAccess(List<String> access) {
        this.access = access;
    }

    public List<String> getMaintain() {
        return maintain;
    }

    public void setMaintain(List<String> maintain) {
        this.maintain = maintain;
    }

    public List<String> getUserSitePermissions() {
        return userSitePermissions;
    }

    public void setUserSitePermissions(List<String> userSitePermissions) {
        this.userSitePermissions = userSitePermissions;
    }
}