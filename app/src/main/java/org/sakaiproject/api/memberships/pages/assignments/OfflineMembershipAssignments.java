package org.sakaiproject.api.memberships.pages.assignments;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 22/02/16.
 */
public class OfflineMembershipAssignments {
    private Context context;
    private final Gson gson = new Gson();
    private String siteId;

    public OfflineMembershipAssignments(Context context, String siteId) {
        this.context = context;
        this.siteId = siteId;
    }

    public void getAssignments(){
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments")) {
            try {
                String a = Actions.readJsonFile(context, siteId + "_assignments", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments");
                Assignment assignment = gson.fromJson(a, Assignment.class);
                JsonParser.getMembershipAssignments(assignment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
