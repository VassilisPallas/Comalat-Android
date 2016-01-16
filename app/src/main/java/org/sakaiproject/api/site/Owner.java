package org.sakaiproject.api.site;

/**
 * Created by vasilis on 1/15/16.
 */
public class Owner {
    private String userDisplayName, userId;

    public Owner(String userDisplayName, String userId) {
        this.userDisplayName = userDisplayName;
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
