package org.sakaiproject.api.announcements;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vspallas on 09/02/16.
 */
public class OfflineUserAnnouncements {
    private Context context;
    private final Gson gson = new Gson();
    private Callback callback;

    public OfflineUserAnnouncements(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void getAnnouncements(String fileName) {
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "announcements")) {
            try {
                String announce = ActionsHelper.readJsonFile(context, fileName, User.getUserEid() + File.separator + "announcements");
                Announcement announcement = gson.fromJson(announce, Announcement.class);
                callback.onSuccess(announcement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
