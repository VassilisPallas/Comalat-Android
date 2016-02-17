package org.sakaiproject.customviews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.sakai.R;

import java.util.List;

/**
 * Created by vspallas on 17/02/16.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    Announcement announcement;

    public AnnouncementAdapter(Announcement announcement) {
        this.announcement = announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        vh.title = (TextView) v.findViewById(R.id.announcement_title);
        vh.owner = (TextView) v.findViewById(R.id.announcement_owner);
        vh.site = (TextView) v.findViewById(R.id.announcement_site);

        return vh;
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapter.ViewHolder holder, int position) {
        holder.title.setText(announcement.getAnnouncementCollection().get(position).getTitle());
        holder.owner.setText(announcement.getAnnouncementCollection().get(position).getCreatedByDisplayName());
        holder.site.setText(announcement.getAnnouncementCollection().get(position).getSiteTitle());
    }

    @Override
    public int getItemCount() {
        return announcement != null && announcement.getAnnouncementCollection() != null ? announcement.getAnnouncementCollection().size() : 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, owner, site;

        public ViewHolder(View v) {
            super(v);
        }
    }
}
