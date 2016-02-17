package org.sakaiproject.sakai;

import org.sakaiproject.api.memberships.SiteData;

/**
 * Created by vasilis on 1/26/16.
 */
public interface IMembershipDialog {
    void openDialog(String userShortName, String email, String shortDescription, String description, SiteData data);
}
