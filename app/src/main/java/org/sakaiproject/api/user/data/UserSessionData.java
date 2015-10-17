package org.sakaiproject.api.user.data;


import java.io.Serializable;

/**
 * Created by vasilis on 10/13/15.
 * This class stores the data from the session json
 * http://url/direct/session/sessionId.json
 */
@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class UserSessionData implements Serializable {

    // Enum attributeNames;
    private String attributes;
    private Integer creationTime;
    private Integer currentTime;
    private String id;
    private Integer lastAccessedTime;
    private Integer maxInactiveInterval;
    private String userEid;
    private String userId;
    private Boolean active;
    private String entityReference;
    private String entityURL;
    private String entityId;

    public UserSessionData() {
    }

    public String getAttributes() {
        return attributes;
    }

    public Integer getCreationTime() {
        return creationTime;
    }

    public Integer getCurrentTime() {
        return currentTime;
    }

    public String getSessionID() {
        return id;
    }

    public Integer getLastAccessedTime() {
        return lastAccessedTime;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public String getUserEid() {
        return userEid;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getActive() {
        return active;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public void setCreationTime(Integer creationTime) {
        this.creationTime = creationTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

    public void setSessionID(String sessionID) {
        this.id = sessionID;
    }

    public void setLastAccessedTime(Integer lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public void setUserEid(String userEid) {
        this.userEid = userEid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public void setEntityURL(String entityURL) {
        this.entityURL = entityURL;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

}
