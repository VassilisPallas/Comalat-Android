package org.sakaiproject.api.memberships.actions;

import org.sakaiproject.api.memberships.SiteData;

import java.util.List;

/**
 * Created by vasilis on 1/24/16.
 */
public interface IUnJoin {
    void siteUnJoin(List<SiteData> membership, int position);
}
