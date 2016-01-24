package org.sakaiproject.api.site.actions;

import org.sakaiproject.api.site.SiteData;

import java.util.List;

/**
 * Created by vasilis on 1/24/16.
 */
public interface IUnJoin {
    void siteUnJoin(List<SiteData> membership, int position);
}
