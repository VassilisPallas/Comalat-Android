package org.sakaiproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by vspallas on 03/03/16.
 */
public class DropboxAdapter extends RecyclerView.Adapter<DropboxAdapter.ViewHolder> {
    private LinkedHashMap<Member, Dropbox> dropboxList;
    private Context context;
    private boolean isRoot;
    private Member clickedMemberDir;
    String siteId;
    private String previousPlace;
    private Dropbox dropbox;

    public DropboxAdapter(LinkedHashMap<Member, Dropbox> dropboxList, Context context, String siteId, boolean isRoot) {
        this.dropboxList = dropboxList;
        this.context = context;
        this.siteId = siteId;
        this.isRoot = isRoot;
    }

    public DropboxAdapter(Dropbox dropbox, Context context, String siteId, boolean isRoot) {
        this.dropbox = dropbox;
        this.context = context;
        this.siteId = siteId;
        this.isRoot = isRoot;
    }

    public void setClickedMemberDir(Member clickedMemberDir) {
        this.clickedMemberDir = clickedMemberDir;
    }

    public void setPreviousPlace(String previousPlace) {
        this.previousPlace = previousPlace;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropbox_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.fileTypeImage = (ImageView) v.findViewById(R.id.image_type);
        vh.fileTypeImageSmall = (ImageView) v.findViewById(R.id.image_type_small);
        vh.fileName = (TextView) v.findViewById(R.id.file_name);
        vh.fileSize = (TextView) v.findViewById(R.id.file_size);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isRoot) {
            Member member = (new ArrayList<>(dropboxList.keySet())).get(position);
            holder.fileTypeImage.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_folder, Color.WHITE));
            holder.fileTypeImageSmall.setVisibility(View.GONE);
            holder.fileName.setText(String.format("%s (%s)", member.getSortName(), member.getEid()));
            holder.fileSize.setVisibility(View.GONE);
        } else {
            if (position == 0) {
                holder.fileTypeImage.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_up, Color.WHITE));
                holder.fileTypeImageSmall.setVisibility(View.GONE);
                holder.fileName.setVisibility(View.GONE);
                holder.fileSize.setVisibility(View.GONE);
            } else {
                Dropbox.Collection collection = dropbox.getCollection().get(position);
                String url = collection.getUrl();
                String ex = url.substring(url.lastIndexOf('.') + 1, url.length());
                if (ex != null) {
                    holder.fileTypeImage.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_folder, Color.WHITE));


                    String fileName = collection.getTitle();
                    if(fileName.length() > 17){
                        String extension = fileName.substring(fileName.lastIndexOf('.'));
                        fileName = fileName.substring(0, 10);
                        holder.fileName.setText(fileName + ".." + extension);
                    }else{
                        holder.fileName.setText(fileName);
                    }

                    holder.fileName.setText(collection.getTitle());
                } else {
                    holder.fileTypeImage.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_insert_drive_file, Color.WHITE));
                }
            }
        }
//        } else if (canGoBack) {
//            holder.fileTypeImage.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_up, Color.WHITE));
//            holder.fileTypeImageSmall.setVisibility(View.GONE);
//            holder.fileName.setVisibility(View.GONE);
//            holder.fileSize.setVisibility(View.GONE);
//        } else {
//
//        }
    }

    @Override
    public int getItemCount() {
        return dropboxList != null ? dropboxList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileTypeImage, fileTypeImageSmall;
        TextView fileName, fileSize;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
