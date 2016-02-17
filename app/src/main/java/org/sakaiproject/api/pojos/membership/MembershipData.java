package org.sakaiproject.api.pojos.membership;

import org.sakaiproject.api.pojos.Time;
import org.sakaiproject.api.pojos.MembershipOwner;

import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 15/02/16.
 */
public class MembershipData {
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
    private MembershipOwner siteOwner;
    private String skin;
    private Object softlyDeletedDate;
    private String title;
    private List<String> userRoles;
    private boolean activeEdit;
    private boolean customPageOrdered;
    private boolean empty;
    private boolean joinable;
    private boolean pubView;
    private boolean published;
    private boolean softlyDeleted;
    private List<SitePage> sitePages;

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

    public MembershipOwner getSiteOwner() {
        return siteOwner;
    }

    public void setSiteOwner(MembershipOwner siteOwner) {
        this.siteOwner = siteOwner;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Object getSoftlyDeletedDate() {
        return softlyDeletedDate;
    }

    public void setSoftlyDeletedDate(Object softlyDeletedDate) {
        this.softlyDeletedDate = softlyDeletedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<SitePage> getSitePages() {
        return sitePages;
    }

    public void setSitePages(List<SitePage> sitePages) {
        this.sitePages = sitePages;
    }
}
