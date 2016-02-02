package org.sakaiproject.sakai;

import android.support.v7.widget.RecyclerView;

import org.sakaiproject.api.pages.syllabus.Syllabus;
import org.sakaiproject.customviews.adapters.SyllabusAdapter;

/**
 * Created by vasilis on 1/28/16.
 */
public interface UpdateSyllabus {
    void update(RecyclerView recyclerView, SyllabusAdapter adapter, Syllabus syllabusData, String id);
}
