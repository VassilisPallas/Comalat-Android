package org.sakaiproject.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by vspallas on 02/03/16.
 */
public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.ViewHolder> {

    private Context context;
    private List<Member> members;
    private SiteData siteData;

    public RosterAdapter(Context context, List<Member> members, SiteData siteData) {
        this.context = context;
        this.members = members;
        this.siteData = siteData;
    }

    public void setRoster(List<Member> members) {
        this.members = members;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.roster_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.image = (ImageViewRounded) v.findViewById(R.id.user_image);
        vh.name = (TextView) v.findViewById(R.id.name);
        vh.eid = (TextView) v.findViewById(R.id.eid);
        vh.role = (TextView) v.findViewById(R.id.role);
        vh.friendStatus = (LinearLayout) v.findViewById(R.id.friend_status);
        vh.addRemove = (ImageView) v.findViewById(R.id.add_remove);
        vh.ignore = (ImageView) v.findViewById(R.id.ignore);
        vh.confirm = (ImageView) v.findViewById(R.id.confirm);
        vh.connectionRequest = (TextView) v.findViewById(R.id.connection_request);
        vh.friendRequest = (LinearLayout) v.findViewById(R.id.friend_request);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Member member = members.get(position);

        if (member.getUserId().equals(User.getUserId())) {
            try {
                holder.image.setImageBitmap(ActionsHelper.getImage(context, "user_thumbnail_image", User.getUserEid() + File.separator + "user"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            try {
                holder.image.setImageBitmap(ActionsHelper.getImage(context, member.getUserId() + "_image", User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster" + File.separator + "user_images"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        holder.name.setText(member.getSortName());
        holder.eid.setText(member.getEid());
        holder.role.setText(member.getRole());

        int status = member.getConnectionStatus();

        if (status == 0 || status == 3) {

            holder.friendRequest.setVisibility(View.GONE);
            if (!member.getUserId().equals(User.getUserId())) {

                holder.friendStatus.setVisibility(View.VISIBLE);

                if (status == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.addRemove.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_join, context.getResources().getColor(R.color.green, context.getTheme())));
                    } else {
                        holder.addRemove.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_join, context.getResources().getColor(R.color.green)));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.addRemove.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_unjoin, context.getResources().getColor(R.color.delete_red, context.getTheme())));
                    } else {
                        holder.addRemove.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_unjoin, context.getResources().getColor(R.color.delete_red)));
                    }
                }

            }
        } else {
            holder.friendStatus.setVisibility(View.GONE);
            holder.friendRequest.setVisibility(View.VISIBLE);

            if (status == 1) {
                holder.connectionRequest.setVisibility(View.VISIBLE);
                holder.friendRequest.setVisibility(View.GONE);
            } else {
                holder.connectionRequest.setVisibility(View.GONE);
                holder.friendRequest.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.confirm.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_check_circle, context.getResources().getColor(R.color.green, context.getTheme())));
                    holder.ignore.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_cancel, context.getResources().getColor(R.color.delete_red, context.getTheme())));
                } else {
                    holder.confirm.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_check_circle, context.getResources().getColor(R.color.green)));
                    holder.ignore.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_cancel, context.getResources().getColor(R.color.delete_red)));
                }
            }
        }

        holder.name.setText(member.getSortName());

    }

    @Override
    public int getItemCount() {
        return members != null ? members.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageViewRounded image;
        TextView name, eid, role, connectionRequest;
        LinearLayout friendStatus, friendRequest;
        ImageView addRemove, ignore, confirm;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
