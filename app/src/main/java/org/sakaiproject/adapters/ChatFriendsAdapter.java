package org.sakaiproject.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.pojos.friends.Friends;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.sakai.R;

/**
 * Created by vspallas on 06/04/16.
 */
public class ChatFriendsAdapter extends RecyclerView.Adapter<ChatFriendsAdapter.ViewHolder> {

    Friends friends;

    public ChatFriendsAdapter(Friends friends) {
        this.friends = friends;
    }

    @Override
    public ChatFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friends.FriendsItem friend = friends.friendsList.get(position);
        Context context = holder.v.getContext();
        //holder.userImage.setImageDrawable();
        holder.name.setText(friend.getDisplayName());
        //holder.lastUnread.setText();
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

    @Override
    public int getItemCount() {
        return 0;
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
