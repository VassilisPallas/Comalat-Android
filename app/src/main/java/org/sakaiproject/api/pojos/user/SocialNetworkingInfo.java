package org.sakaiproject.api.pojos.user;

import java.io.Serializable;

/**
 * Created by vasilis on 10/15/15.
 * This has created to get the data which are SocialNetworkingInfo type from the response jsons
 */
public class SocialNetworkingInfo implements Serializable {

    private String facebookUrl;
    private String linkedinUrl;
    private String myspaceUrl;
    private String skypeUsername;
    private String twitterUrl;

    public SocialNetworkingInfo() {

    }

    public SocialNetworkingInfo(String facebookUrl, String linkedinUrl, String myspaceUrl, String skypeUsername, String twitterUrl) {
        this.facebookUrl = facebookUrl;
        this.linkedinUrl = linkedinUrl;
        this.myspaceUrl = myspaceUrl;
        this.skypeUsername = skypeUsername;
        this.twitterUrl = twitterUrl;
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
