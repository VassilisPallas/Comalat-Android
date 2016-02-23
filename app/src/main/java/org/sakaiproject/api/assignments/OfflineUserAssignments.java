package org.sakaiproject.api.assignments;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 22/02/16.
 */
public class OfflineUserAssignments {
    private Context context;
    private final Gson gson = new Gson();

    public OfflineUserAssignments(Context context) {
        this.context = context;
    }

    public void getAssignments() {
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "assignments")) {
            try {
                String a = Actions.readJsonFile(context, "assignments", User.getUserEid() + File.separator + "assignments");
                Assignment assignment = gson.fromJson(a, Assignment.class);
                JsonParser.getUserAssignments(assignment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
