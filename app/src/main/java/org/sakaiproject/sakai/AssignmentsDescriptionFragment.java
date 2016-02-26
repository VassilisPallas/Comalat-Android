package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.AttachmentType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by vspallas on 23/02/16.
 */
public class AssignmentsDescriptionFragment extends DialogFragment {

    private TextView title, dueDate, submissionType, allowResubmission, closeDate, status, gradeScale, lastModified, modelAnswer, privateNotes, allPurposeItem;
    private RichTextView instructions;
    private LinearLayout attachmentsLinear;

    private Assignment.AssignmentsCollection assignment;
    private SiteData siteData;

    public AssignmentsDescriptionFragment() {
    }

    public AssignmentsDescriptionFragment setData(Assignment.AssignmentsCollection assignment, SiteData siteData) {
        AssignmentsDescriptionFragment announcementDescriptionFragment = new AssignmentsDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("assignment", assignment);
        bundle.putSerializable("siteData", siteData);
        announcementDescriptionFragment.setArguments(bundle);
        return announcementDescriptionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment_description, container, false);

        assignment = (Assignment.AssignmentsCollection) getArguments().getSerializable("assignment");
        siteData = (SiteData) getArguments().getSerializable("siteData");

        title = (TextView) v.findViewById(R.id.assignment_title);
        title.setText(assignment.getTitle());

        dueDate = (TextView) v.findViewById(R.id.due_date);
        dueDate.setText(assignment.getDueTimeString());

        submissionType = (TextView) v.findViewById(R.id.submission);
        submissionType.setText(assignment.getSubmissionType());

        allowResubmission = (TextView) v.findViewById(R.id.allow_resubmissions);
        allowResubmission.setText(assignment.isAllowResubmission() ? getContext().getResources().getString(R.string.yes) : getContext().getResources().getString(R.string.no));

        closeDate = (TextView) v.findViewById(R.id.close_date);
        closeDate.setText(assignment.getCloseTimeString());

        status = (TextView) v.findViewById(R.id.status);
        status.setText(assignment.getStatus());

        gradeScale = (TextView) v.findViewById(R.id.grades_scale);

        gradeScale.setText(assignment.getGradeScale());
        if (assignment.getGradeScale().equals(getContext().getResources().getString(R.string.points))) {
            gradeScale.append(" (");
            gradeScale.append(assignment.getGradeScaleMaxPoints());
            gradeScale.append(" )");
        } else if (assignment.getGradeScale().equals(getContext().getResources().getString(R.string.letter_grade))) {
            gradeScale.setText("A-F");
        } else if (assignment.getGradeScale().equals(getContext().getResources().getString(R.string.pass_fail))) {
            gradeScale.setText("P-F");
        } else if (assignment.getGradeScale().equals(getContext().getResources().getString(R.string.checkmark))) {
            gradeScale.setText("✔");
        }

        lastModified = (TextView) v.findViewById(R.id.modified);
        lastModified.setText(assignment.getTimeLastModified().getDisplay());

        modelAnswer = (TextView) v.findViewById(R.id.model_answer);
        if (assignment.getModelAnswerText() != null)
            modelAnswer.setText(assignment.getModelAnswerText());
        else {
            v.findViewById(R.id.model_row).setVisibility(View.GONE);
            v.findViewById(R.id.model_txt_row).setVisibility(View.GONE);
        }

        privateNotes = (TextView) v.findViewById(R.id.private_notes);
        if (assignment.getPrivateNoteText() != null)
            privateNotes.setText(assignment.getPrivateNoteText());
        else {
            v.findViewById(R.id.private_notes_txt_row).setVisibility(View.GONE);
            v.findViewById(R.id.private_notes_row).setVisibility(View.GONE);
        }

        allPurposeItem = (TextView) v.findViewById(R.id.all_purpose_item);
        if (assignment.getAllPurposeItemText() != null)
            allPurposeItem.setText(assignment.getAllPurposeItemText());
        else {
            v.findViewById(R.id.all_purpose_item_txt_row).setVisibility(View.GONE);
            v.findViewById(R.id.all_purpose_item_row).setVisibility(View.GONE);
        }

        instructions = (RichTextView) v.findViewById(R.id.instructions);
        instructions.setContext(getContext());
        if (siteData != null)
            instructions.setSiteData(siteData.getId());
        else
            instructions.setSiteData(assignment.getContext());

        String instruct = assignment.getInstructions();
        instruct = ActionsHelper.deleteHtmlTags(instruct);
        instructions.setText(instruct);

        attachmentsLinear = (LinearLayout) v.findViewById(R.id.attachments_linear);
        if (assignment.getAttachments().size() > 0) {
            for (int i = 0; i < assignment.getAttachments().size(); i++) {
                View currentAttachment = attachmentsLinear.inflate(getActivity(), R.layout.attachment_row, null);

                TextView name = (TextView) currentAttachment.findViewById(R.id.attachment_name);
                String url = assignment.getAttachments().get(i).getUrl();
                try {
                    if (ActionsHelper.getAttachmentType(url) != AttachmentType.URL) {
                        name.setText(URLDecoder.decode(url.replace("_", "/"), "UTF-8").substring(url.lastIndexOf('/') + 1));
                    } else {
                        name.setText(URLDecoder.decode(url, "UTF-8").replaceAll("_", "/"));
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                ImageView image = (ImageView) currentAttachment.findViewById(R.id.attachment_type_image);

                image.setImageBitmap(ActionsHelper.getAttachmentTypeImage(getContext(), assignment.getAttachments().get(i).getUrl()));

                attachmentsLinear.addView(currentAttachment);
            }
        } else {
            v.findViewById(R.id.attachment_row).setVisibility(View.GONE);
            v.findViewById(R.id.attachment_txt_row).setVisibility(View.GONE);
        }

        return v;
    }

}
