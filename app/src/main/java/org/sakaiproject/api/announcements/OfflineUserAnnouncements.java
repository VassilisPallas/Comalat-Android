package org.sakaiproject.api.announcements;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 09/02/16.
 */
public class OfflineUserAnnouncements {
    private Context context;
    private final Gson gson = new Gson();

    public OfflineUserAnnouncements(Context context) {
        this.context = context;
    }

    public void getAnnouncements() {
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements")) {
            try {
                String announce = Actions.readJsonFile(context, "announcements", User.getUserEid() + File.separator + "announcements");
                Announcement announcement = gson.fromJson(announce, Announcement.class);
                JsonParser.getUserAnnouncements(announcement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
