package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.AttachmentType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * Created by vspallas on 22/02/16.
 */
public class AnnouncementDescriptionFragment extends DialogFragment {

    private Announcement.AnnouncementItems announcement;
    private TextView title;
    private RichTextView body;
    private LinearLayout attachments;

    public AnnouncementDescriptionFragment() {
    }

    public AnnouncementDescriptionFragment setData(Announcement.AnnouncementItems announcement) {
        AnnouncementDescriptionFragment announcementDescriptionFragment = new AnnouncementDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("announcement", announcement);
        announcementDescriptionFragment.setArguments(bundle);
        return announcementDescriptionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_announcement_description, container, false);

        announcement = (Announcement.AnnouncementItems) getArguments().getSerializable("announcement");

        title = (TextView) v.findViewById(R.id.announcement_title);
        body = (RichTextView) v.findViewById(R.id.announcement_body);

        title.setText(announcement.getTitle());
        body.setText(announcement.getBody());

        if (announcement.getAttachments().size() > 0) {

            attachments = (LinearLayout) v.findViewById(R.id.attachments_linear);
            attachments.setVisibility(View.VISIBLE);

            for (int i = 0; i < announcement.getAttachments().size(); i++) {
                View currentAttachment = attachments.inflate(getActivity(), R.layout.attachment_row, null);

                TextView name = (TextView) currentAttachment.findViewById(R.id.attachment_name);
                String url = announcement.getAttachments().get(i).getUrl();
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

                image.setImageBitmap(ActionsHelper.getAttachmentTypeImage(getContext(), announcement.getAttachments().get(i).getUrl()));

                attachments.addView(currentAttachment);
            }
        }

        return v;
    }
}
