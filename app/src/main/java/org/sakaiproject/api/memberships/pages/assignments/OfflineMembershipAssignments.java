package org.sakaiproject.api.memberships.pages.assignments;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 22/02/16.
 */
public class OfflineMembershipAssignments {
    private Context context;
    private final Gson gson = new Gson();
    private String siteId;
    private Callback callback;

    public OfflineMembershipAssignments(Context context, String siteId, Callback callback) {
        this.context = context;
        this.siteId = siteId;
        this.callback = callback;
    }

    public void getAssignments() {
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments")) {
            try {
                String a = ActionsHelper.readJsonFile(context, siteId + "_assignments", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "assignments");
                Assignment assignment = gson.fromJson(a, Assignment.class);
                callback.onSuccess(assignment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
