package com.justplay.android.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.justplay.android.R;
import com.justplay.android.databinding.ItemMediaBinding;
import com.justplay.android.model.MediaItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {

    private List<MediaItemViewModel> mediaItems = new ArrayList<>();
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemMediaBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ItemMediaBinding getBinding() {
            return binding;
        }

        @BindingAdapter("bind:imageUrl")
        public static void setMediaImageView(ImageView imageView, String imageUrl) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemMediaBinding binding = holder.getBinding();
        binding.setModel(mediaItems.get(position));
        binding.setListener(v -> listener.onItemClicked(position));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mediaItems != null ? mediaItems.size() : 0;
    }

    public void update(List<MediaItemViewModel> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
