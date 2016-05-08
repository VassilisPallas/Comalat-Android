package org.sakaiproject.sakai;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.announcements.OfflineUserAnnouncements;
import org.sakaiproject.api.announcements.UserAnnouncementsService;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.general.AttachmentType;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment implements Callback {

    private static final int TASK_COMPLETE = 1;
    private ProgressBar progressBar;
    private Callback callback = this;
    private Handler mHandler;
    private RichTextView motd;
    private ImageView motdImage;
    private LinearLayout attachments;

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        getActivity().setTitle(getContext().getResources().getString(R.string.welcome));

        findViewsById(v);

        getMotd();

        return v;
    }

    private void getMotd() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                if (NetWork.getConnectionEstablished()) {
                    UserAnnouncementsService motd = new UserAnnouncementsService(getContext(), callback);
                    motd.getAnnouncements(getContext().getResources().getString(R.string.url) + "announcement/motd.json", "motd");
                } else {
                    OfflineUserAnnouncements motd = new OfflineUserAnnouncements(getContext(), callback);
                    motd.getAnnouncements("motd");
                }

                message.what = TASK_COMPLETE;
                mHandler.sendMessage(message);
            }
        }).start();

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case TASK_COMPLETE:
                        progressBar.setVisibility(View.GONE);
                        return true;
                    default:
                        handleMessage(msg);
                        return false;
                }
            }
        });
    }

    private void findViewsById(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.motd_progressbar);

        RichTextView welcomeMessage = (RichTextView) v.findViewById(R.id.welcome_message);
        welcomeMessage.setContext(getContext());

        motd = (RichTextView) v.findViewById(R.id.motd);
        motd.setContext(getContext());

        motdImage = (ImageView) v.findViewById(R.id.message_of_the_day_image);
        attachments = (LinearLayout) v.findViewById(R.id.attachments_linear);
    }

    @Override
    public void onSuccess(Object obj) {
        if (obj instanceof Announcement) {
            Announcement announcement = (Announcement) obj;
            if (announcement.getAnnouncementCollection() != null && announcement.getAnnouncementCollection().size() != 0) {
                Announcement.AnnouncementItems announcementItem = announcement.getAnnouncementCollection().get(0);
                motdImage.setVisibility(View.GONE);
                motd.setText(announcementItem.getBody());
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
                motd.setText(getString(R.string.motd));
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
