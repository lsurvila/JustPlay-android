package com.justplay.android.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.justplay.android.adapter.MediaItemAdapter;
import com.justplay.android.R;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends RxFragment {

    private static final int PERMISSION_REQUEST = 1;
    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    private JustPlayApi api;
    private int requestedItemPosition;

    private MediaItemAdapter adapter;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        api = new JustPlayApi();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mediaGrid.setLayoutManager(layoutManager);
        adapter = new MediaItemAdapter();
        adapter.setOnItemClickListener(position -> {
            requestedItemPosition = position;
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivityFragment.this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                MainActivityFragment.this.downloadMediaItem(requestedItemPosition);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityFragment.this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    MainActivityFragment.this.showPermissionWarning();
                } else {
                    MainActivityFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
                }
            }
        });
        mediaGrid.setAdapter(adapter);
        return view;
    }

    private void showPermissionWarning() {
        Toast.makeText(getContext(), "In order to save media files to disk, permission must be turned on", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadMediaItem(requestedItemPosition);
                } else {
                    showPermissionWarning();
                }
        }
    }

    private void downloadMediaItem(int posision) {
        SearchResponse item = adapter.getItem(posision);
        adapter.notifyItemChanged(posision);
        item.setIsDownloading(true);
        api.download(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilFragmentEvent(lifecycle(), FragmentEvent.DESTROY))
                .subscribe(file -> {
                    item.setIsDownloading(false);
                    adapter.notifyItemChanged(posision);
                    Snackbar.make(mediaGrid, "Downloaded to " + file.getAbsolutePath(), Snackbar.LENGTH_SHORT).show();
                }, error -> {
                    item.setIsDownloading(false);
                    adapter.notifyItemChanged(posision);
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void updateGrid(List<SearchResponse> mediaItems) {
        adapter.update(mediaItems);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
