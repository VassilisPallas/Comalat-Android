package org.sakaiproject.api.sync;

import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public interface DropboxRefreshUI {
    void updateUI(Map<String, Integer> dropboxList);
}
