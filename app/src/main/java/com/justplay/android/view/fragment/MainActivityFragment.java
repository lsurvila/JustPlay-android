package com.justplay.android.view.fragment;

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

import com.justplay.android.JustPlayApplication;
import com.justplay.android.component.DaggerMediaDownloadComponent;
import com.justplay.android.component.MediaDownloadComponent;
import com.justplay.android.module.MediaDownloadModule;
import com.justplay.android.view.adapter.MediaItemAdapter;
import com.justplay.android.R;
import com.justplay.android.view.adapter.OnItemClickListener;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.presenter.MediaDownloadPresenter;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivityFragment extends RxFragment implements OnItemClickListener, MediaGridView {

    private static final int PERMISSION_REQUEST = 1;
    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    private int requestedItemPosition;

    private MediaItemAdapter adapter;
    private MediaDownloadPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        MediaDownloadComponent mediaComponent = DaggerMediaDownloadComponent.builder()
                .applicationComponent(JustPlayApplication.component())
                .mediaDownloadModule(new MediaDownloadModule(this))
                .build();
        presenter = mediaComponent.downloadPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                    presenter.downloadMediaItem(requestedItemPosition, adapter.getItem(requestedItemPosition));
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
