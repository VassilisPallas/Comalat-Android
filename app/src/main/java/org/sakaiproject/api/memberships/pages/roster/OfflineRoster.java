package org.sakaiproject.api.memberships.pages.roster;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 03/03/16.
 */
public class OfflineRoster {
    private Context context;
    private SiteData siteData;
    private Gson gson = new Gson();
    private Callback callback;

    public OfflineRoster(Context context, SiteData siteData, Callback callback) {
        this.context = context;
        this.siteData = siteData;
        this.callback = callback;
    }

    public void getRoster() {
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster")) {
            String json = null;
            try {
                json = ActionsHelper.readJsonFile(context, siteData.getId() + "_roster", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Roster roster = gson.fromJson(json, Roster.class);
            callback.onSuccess(roster);
        }
    }
}
