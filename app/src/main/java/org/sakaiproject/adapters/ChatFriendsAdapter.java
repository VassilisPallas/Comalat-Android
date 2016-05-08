package org.sakaiproject.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.pojos.friends.Friends;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by vspallas on 06/04/16.
 */
public class ChatFriendsAdapter extends RecyclerView.Adapter<ChatFriendsAdapter.ViewHolder> {

    private List<Friends> friends;

    public ChatFriendsAdapter(List<Friends> friends) {
        this.friends = friends;
    }

    @Override
    public ChatFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (friends != null) {
            Friends friend = friends.get(position);
            Context context = holder.v.getContext();

            try {
                holder.userImage.setImageBitmap(ActionsHelper.getImage(holder.v.getContext(), friends.get(position).getFriendId() + "_image", User.getUserEid() + File.separator + "friends" + File.separator + friends.get(position).getFriendId()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            holder.name.setText(friend.getDisplayName());

            if (friend.getOnlineStatus() == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.status.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_offline, context.getTheme()));
                } else {
                    holder.status.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_offline));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.status.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_online, context.getTheme()));
                } else {
                    holder.status.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_online));
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageViewRounded userImage;
        TextView name, lastUnread;
        ImageView status;
        View v;

        public ViewHolder(View view) {
            super(view);
            v = view;
            userImage = (ImageViewRounded) view.findViewById(R.id.user_image);
            name = (TextView) view.findViewById(R.id.name);
            lastUnread = (TextView) view.findViewById(R.id.last_unread_text);
            status = (ImageView) view.findViewById(R.id.status);
        }
    }
}
