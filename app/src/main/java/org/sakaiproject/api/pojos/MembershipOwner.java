package org.sakaiproject.api.pojos;

import java.io.Serializable;

/**
 * Created by vasilis on 1/15/16.
 * custom calls for the site owner
 */
public class MembershipOwner implements Serializable {

    private String userDisplayName, userId;

    /**
     * Owner constructor
     *
     * @param userDisplayName the display name from the user
     * @param userId          the userId
     */
    public MembershipOwner(String userDisplayName, String userId) {
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
