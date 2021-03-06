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
import butterknife.OnClick;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {

    private List<SearchResponse> mediaItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.media_item)
        View mediaItemView;
        @Bind(R.id.media_image)
        ImageView mediaImageView;
        @Bind(R.id.media_title)
        TextView mediaTitleView;
        @Bind(R.id.media_downloading)
        View progressView;

        private final OnItemClickListener listener;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.media_item)
        void onItemClicked() {
            if (listener != null) {
                listener.onItemClicked(getLayoutPosition());
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_media, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResponse item = getItem(position);
        Glide.with(context)
                .load(item.getImageUrl())
                .centerCrop()
                .crossFade()
                .into(holder.mediaImageView);
        holder.mediaTitleView.setText(item.getTitle());
        holder.progressView.setVisibility(item.isDownloading() ? View.VISIBLE : View.INVISIBLE);
    }

    public SearchResponse getItem(int position) {
        return mediaItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public void update(List<SearchResponse> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
