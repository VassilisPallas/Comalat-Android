package org.sakaiproject.api.memberships.pages.syllabus;

import android.content.Context;

import com.google.gson.Gson;

import org.sakaiproject.api.pojos.syllabus.Syllabus;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 1/28/16.
 */
public class OfflineSyllabus {
    private Context context;
    private String siteId;
    private final Gson gson = new Gson();

    public OfflineSyllabus(Context context, String siteId) {
        this.siteId = siteId;
        this.context = context;
    }

    public Syllabus getSyllabus() throws IOException {
        String syllabusJson = null;
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
            syllabusJson = ActionsHelper.readJsonFile(context, siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");

        Syllabus syllabus = gson.fromJson(syllabusJson, Syllabus.class);

        return syllabus;
    }

}
