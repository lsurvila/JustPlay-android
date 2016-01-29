package com.justplay.android.mediagrid.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.JustPlayApplication;
import com.justplay.android.mediagrid.dagger.DaggerMediaGridComponent;
import com.justplay.android.mediagrid.dagger.MediaGridComponent;
import com.justplay.android.mediagrid.model.MediaItemViewModel;
import com.justplay.android.helper.PermissionManager;
import com.justplay.android.helper.PresenterCache;
import com.justplay.android.mediagrid.view.adapter.MediaItemAdapter;
import com.justplay.android.R;
import com.justplay.android.mediagrid.view.adapter.OnItemClickListener;
import com.justplay.android.mediagrid.presenter.MediaGridPresenter;
import com.justplay.android.mediagrid.view.MediaGridView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivityFragment extends Fragment implements OnItemClickListener, MediaGridView, PermissionManager.PermissionCallback {

    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    @Bind(R.id.media_progress)
    View progressBar;

    @Inject PresenterCache presenterCache;
    @Inject MediaItemAdapter adapter;
    @Inject PermissionManager permissionManager;
    private MediaGridPresenter presenter;

    private Callback callback;

    private boolean onSaveInstanceCalled;
    private boolean onDestroyCalled;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        MediaGridComponent mediaComponent = DaggerMediaGridComponent.builder()
                .applicationComponent(JustPlayApplication.component())
                .build();
        mediaComponent.inject(this);
        adapter.setOnItemClickListener(this);
        presenter = presenterCache.getPresenter();
        if (presenter == null) {
            presenter = mediaComponent.mediaGridPresenter();
        }
        presenter.bindView(this);
        permissionManager.setCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mediaGrid.setLayoutManager(layoutManager);
        mediaGrid.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // we restore view state (either new or restored presenter, we bind existing data to view from preserved model inside presenter)
        presenter.updateViewState();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!onDestroyCalled) {
            // if destroy was not called (activity just minimised, but onSaveInstanceState still called), we don't need to restore and therefore ignore onSaveInstanceState
            onSaveInstanceCalled = false;
        }
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        presenter.searchMedia(queryTextEvents.flatMap(searchViewQueryTextEvent -> {
            if (searchViewQueryTextEvent.isSubmitted()) {
                return Observable.just(searchViewQueryTextEvent.queryText().toString());
            }
            return Observable.empty();
        }).observeOn(AndroidSchedulers.mainThread()));
    }

    @Override
    public void onItemClicked(int position) {
        permissionManager.requestPermissionIfNeeded(position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.handleResponse(requestCode, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        presenter.downloadMediaItemAllowed(requestCode);
    }

    @Override
    public void onPermissionDenied() {
        presenter.downloadMediaItemDenied();
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

    public void updateGrid(List<MediaItemViewModel> mediaItems) {
        adapter.update(mediaItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            callback.onMediaSearchRequested();
        }
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setGridVisible(boolean visible) {
        mediaGrid.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        presenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        onSaveInstanceCalled = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        onDestroyCalled = true;
        if (onSaveInstanceCalled) {
            // in case view is destroyed by system, we should preserve presenter (with ongoing operations) and unbind view (as a new one will be provided)
            presenter.unbindView();
            presenterCache.savePresenter(presenter);
        } else {
            // in case view is destroyed by user (like back pressed), we should remove presenter (with ongoing operations)
            presenter.unsubscribe();
            presenter.unbindView();
            presenterCache.removePresenter();
        }
        super.onDestroy();
    }

    // ----- Don't keep activities OFF -----
    // - Minimise
    // onSaveInstanceState
    // - Maximise
    // (nothing)
    // 1) NOTHING SHOULD BE DONE, FLAG SHOULD BE IGNORED (view is not destroyed)

    // - Rotate
    // OnSaveInstanceState
    // OnDestroy
    // - After Rotation
    // OnActivityCreated
    // 2) PRESENTER SHOULD BE SAVED, UNBIND VIEW (view is destroyed by SYSTEM) AND BIND VIEW ON CREATE

    // - Back Pressed (Destroy)
    // OnDestroy
    // - Start Again
    // OnActivityCreated
    // 3) DESTROY PRESENTER, UNBIND VIEW, STOP SUBSCRIPTIONS (view is destroyed by USER)


    // ----- Don't keep activities ON -----
    // - Minimise
    // OnSaveInstanceState
    // OnDestroy
    // - Maximise
    // OnActivityCreated
    // 2) PRESENTER SHOULD BE SAVED, UNBIND VIEW (view is destroyed by SYSTEM) AND BIND VIEW ON CREATE

    // - Rotate
    // OnSaveInstanceState
    // OnDestroy
    // - After Rotation
    // OnActivityCreated
    // 2) PRESENTER SHOULD BE SAVED, UNBIND VIEW (view is destroyed by SYSTEM) AND BIND VIEW ON CREATE

    // - Back Pressed (Destroy)
    // OnDestroy
    // - Start Again
    // OnActivityCreated
    // 3) DESTROY PRESENTER, UNBIND VIEW, STOP SUBSCRIPTIONS (view is destroyed by USER)

    public interface Callback {
        void onMediaSearchRequested();
    }

}
