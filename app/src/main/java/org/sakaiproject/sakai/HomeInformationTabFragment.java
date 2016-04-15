package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.announcements.OfflineUserAnnouncements;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.customviews.rich_textview.Image;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.general.AttachmentType;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by vassilis on 4/14/16.
 */
public class HomeInformationTabFragment extends Fragment implements Callback {

    private TextView title;
    private RichTextView message;
    private ImageView motdImage;
    private LinearLayout attachments;
    private SiteData siteData;
    private String siteName;
    private Callback callback = this;
    private CardView workspaceInfoCardview;
    private RichTextView workspaceInfoText;

    public HomeInformationTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_information, container, false);

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        initialize(v);
        showMessage();

        return v;
    }

    private void initialize(View v) {
        title = (TextView) v.findViewById(R.id.title);

        message = (RichTextView) v.findViewById(R.id.message);
        message.setContext(getContext());
        message.setSiteData(siteData.getId());

        motdImage = (ImageView) v.findViewById(R.id.message_of_the_day_image);
        attachments = (LinearLayout) v.findViewById(R.id.attachments_linear);

        workspaceInfoCardview = (CardView) v.findViewById(R.id.workspace_info_cardview);
        workspaceInfoText = (RichTextView) v.findViewById(R.id.workspace_info_text);
        workspaceInfoText.setContext(getContext());
    }

    private void showMessage() {
        if (siteName.equals(getString(R.string.my_workspace))) {
            title.setText(getString(R.string.message_of_the_day));
            OfflineUserAnnouncements motd = new OfflineUserAnnouncements(getContext(), callback);
            motd.getAnnouncements("motd");
            workspaceInfoCardview.setVisibility(View.VISIBLE);
        } else {
            workspaceInfoCardview.setVisibility(View.GONE);
            motdImage.setVisibility(View.GONE);
            title.setText(getString(R.string.site_info_display));
            String description = siteData.getDescription();

            if (description != null) {
                description = ActionsHelper.deleteHtmlTags(description);
                message.setText(description);
            } else {
                message.setText(getContext().getResources().getString(R.string.no_descr));
            }
        }
    }

    @Override
    public void onSuccess(Object obj) {
        if (obj instanceof Announcement) {
            Announcement announcement = (Announcement) obj;
            if (announcement.getAnnouncementCollection() != null && announcement.getAnnouncementCollection().size() != 0) {
                Announcement.AnnouncementItems announcementItem = announcement.getAnnouncementCollection().get(0);
                motdImage.setVisibility(View.GONE);
                message.setText(announcementItem.getBody());
                if (announcementItem.getAttachments() != null && announcementItem.getAttachments().size() != 0) {
                    for (int i = 0; i < announcementItem.getAttachments().size(); i++) {
                        View currentAttachment = attachments.inflate(getActivity(), R.layout.attachment_row, null);

                        TextView name = (TextView) currentAttachment.findViewById(R.id.attachment_name);
                        String url = announcementItem.getAttachments().get(i).getUrl();
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

                        image.setImageBitmap(ActionsHelper.getAttachmentTypeImage(getContext(), announcementItem.getAttachments().get(i).getUrl()));

                        attachments.addView(currentAttachment);
                    }
                }
            } else {
                message.setText(getContext().getResources().getString(R.string.motd));
                motdImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
    }
}
