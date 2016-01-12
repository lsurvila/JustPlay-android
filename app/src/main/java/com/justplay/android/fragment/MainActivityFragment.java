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
import com.justplay.android.adapter.OnItemClickListener;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends RxFragment implements OnItemClickListener, MediaGridView {

    private static final int PERMISSION_REQUEST = 1;
    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    private JustPlayApi api;
    private int requestedItemPosition;

    private MediaItemAdapter adapter;
    private MediaGridPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        api = new JustPlayApi();
        presenter = new MediaGridPresenter(this, api);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mediaGrid.setLayoutManager(layoutManager);
        adapter = new MediaItemAdapter();
        adapter.setOnItemClickListener(this);
        mediaGrid.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClicked(int position) {
        requestedItemPosition = position;
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            presenter.downloadMediaItem(position, adapter.getItem(position));
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionWarning();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
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

    @Override
    public void invalidateItemState(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(mediaGrid, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Observable<FragmentEvent> getLifecycle() {
        return lifecycle();
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

    public interface GridFragmentCallbacks {
        void onItemClicked(int position);
    }

}
