package com.justplay.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {

    private List<SearchResponse> mediaItems = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.media_image)
        ImageView mediaImageView;
        @Bind(R.id.media_title)
        TextView mediaTitleView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mediaTitleView.setText(mediaItems.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public void update(List<SearchResponse> mediaItems) {
        this.mediaItems = mediaItems;
    }

}
