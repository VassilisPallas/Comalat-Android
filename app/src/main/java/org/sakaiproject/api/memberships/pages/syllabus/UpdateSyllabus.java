package org.sakaiproject.api.memberships.pages.syllabus;

import android.support.v7.widget.RecyclerView;

import org.sakaiproject.api.pojos.syllabus.Syllabus;
import org.sakaiproject.adapters.SyllabusAdapter;

/**
 * Created by vasilis on 1/28/16.
 */
public interface UpdateSyllabus {
    void update(RecyclerView recyclerView, SyllabusAdapter adapter, Syllabus syllabusData, String id);
}
