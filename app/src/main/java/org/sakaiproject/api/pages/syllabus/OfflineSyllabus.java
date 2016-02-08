package org.sakaiproject.api.pages.syllabus;

import android.content.Context;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;

/**
 * Created by vasilis on 1/28/16.
 */
public class OfflineSyllabus {
    private Context context;
    private JsonParser jsonParse;
    private String siteId;

    public OfflineSyllabus(Context context, String siteId) {
        this.siteId = siteId;
        this.context = context;
        jsonParse = new JsonParser(context);
    }

    public Syllabus getSyllabus() throws IOException {
        String syllabusJson = null;
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus"))
            syllabusJson = Actions.readJsonFile(context, siteId + "_syllabus", User.getUserEid() + File.separator + "memberships" + File.separator + siteId + File.separator + "syllabus");
        return jsonParse.getSiteSyllabus(syllabusJson);
    }

}
