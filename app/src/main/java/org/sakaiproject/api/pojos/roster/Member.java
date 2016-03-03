package org.sakaiproject.api.pojos.roster;

import java.util.List;

/**
 * Created by vspallas on 02/03/16.
 */
public class Member {
    private int connectionStatus;
    private Object credits;
    private String displayId;
    private String displayName;
    private String eid;
    private String email;
    private String enrollmentStatusId;
    private String enrollmentStatusText;
    //private List<Object> groups;
    private String groupsToString;
    private int lastVisitTime;
    private String role;
    private String sortName;
    private String totalSiteVisits;
    private String userId;

    public int getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Object getCredits() {
        return credits;
    }

    public void setCredits(Object credits) {
        this.credits = credits;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnrollmentStatusId() {
        return enrollmentStatusId;
    }

    public void setEnrollmentStatusId(String enrollmentStatusId) {
        this.enrollmentStatusId = enrollmentStatusId;
    }

    public String getEnrollmentStatusText() {
        return enrollmentStatusText;
    }

    public void setEnrollmentStatusText(String enrollmentStatusText) {
        this.enrollmentStatusText = enrollmentStatusText;
    }

    public String getGroupsToString() {
        return groupsToString;
    }

    public void setGroupsToString(String groupsToString) {
        this.groupsToString = groupsToString;
    }

    public int getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(int lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getTotalSiteVisits() {
        return totalSiteVisits;
    }

    public void setTotalSiteVisits(String totalSiteVisits) {
        this.totalSiteVisits = totalSiteVisits;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
