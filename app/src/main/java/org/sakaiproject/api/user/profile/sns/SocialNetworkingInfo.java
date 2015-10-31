package org.sakaiproject.api.user.profile.sns;

import java.io.Serializable;

/**
 * Created by vasilis on 10/15/15.
 */
public class SocialNetworkingInfo implements Serializable {

    private String facebookUrl;
    private String linkedinUrl;
    private String myspaceUrl;
    private String skypeUsername;
    private String twitterUrl;
    private String userUuid;

    private static final long serialVersionUID = 1L;


    public SocialNetworkingInfo(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getMyspaceUrl() {
        return myspaceUrl;
    }

    public String getSkypeUsername() {
        return skypeUsername;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public void setMyspaceUrl(String myspaceUrl) {
        this.myspaceUrl = myspaceUrl;
    }

    public void setSkypeUsername(String skypeUsername) {
        this.skypeUsername = skypeUsername;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

}
