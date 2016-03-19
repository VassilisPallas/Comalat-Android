package org.sakaiproject.api.assignments;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.sync.AssignmentsRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 22/02/16.
 */
public class OfflineUserAssignments {
    private Context context;
    private final Gson gson = new Gson();
    private AssignmentsRefreshUI delegate;

    public OfflineUserAssignments(Context context, AssignmentsRefreshUI delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void getAssignments() {
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "assignments")) {
            try {
                String a = ActionsHelper.readJsonFile(context, "assignments", User.getUserEid() + File.separator + "assignments");
                Assignment assignment = gson.fromJson(a, Assignment.class);
                delegate.updateUI(assignment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
