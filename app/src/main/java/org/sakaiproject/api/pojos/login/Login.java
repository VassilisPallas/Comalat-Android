package org.sakaiproject.api.pojos.login;

/**
 * Created by vspallas on 09/02/16.
 */
public class Login {

    private Long creationTime;
    private Long lastAccessedTime;
    // max seconds the session can be idle before automatic invalidation
    private Integer maxInactiveInterval;
    private String userEid;
    private String userId;

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(Long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public String getUserEid() {
        return userEid;
    }

    public void setUserEid(String userEid) {
        this.userEid = userEid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
