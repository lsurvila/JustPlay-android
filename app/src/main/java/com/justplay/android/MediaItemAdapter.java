package com.justplay.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {

    private List<SearchResponse> mediaItems = new ArrayList<>();
    private Context context;

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
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResponse item = mediaItems.get(position);
        Glide.with(context)
                .load(item.getImageUrl())
                .centerCrop()
                .crossFade()
                .into(holder.mediaImageView);
        holder.mediaTitleView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public void update(List<SearchResponse> mediaItems) {
        this.mediaItems = mediaItems;
    }

}
