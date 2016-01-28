package org.sakaiproject.api.pages.syllabus;

import android.content.Context;

import org.sakaiproject.api.json.JsonParser;
import org.sakaiproject.general.Actions;

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
        String syllabusJson = Actions.readJsonFile(context, siteId + "_syllabusJson");
        return jsonParse.getSiteSyllabus(syllabusJson);
    }
    
}
