package org.sakaiproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by vassilis on 5/29/16.
 */
public class EmptyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}