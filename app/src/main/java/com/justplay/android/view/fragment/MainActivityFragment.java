package com.justplay.android.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.justplay.android.ApplicationComponent;
import com.justplay.android.JustPlayApplication;
import com.justplay.android.component.DaggerMediaGridComponent;
import com.justplay.android.component.MediaGridComponent;
import com.justplay.android.model.MediaItemViewModel;
import com.justplay.android.presenter.PresenterCache;
import com.justplay.android.view.adapter.MediaItemAdapter;
import com.justplay.android.R;
import com.justplay.android.view.adapter.OnItemClickListener;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivityFragment extends RxFragment implements OnItemClickListener, MediaGridView {

    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    @Bind(R.id.media_progress)
    View progressBar;

    private MediaItemAdapter adapter;
    private MediaGridPresenter presenter;
    private Callback callback;
    private PresenterCache presenterCache;

    private boolean stateSaved;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        ApplicationComponent appComponent = JustPlayApplication.component();
        MediaGridComponent mediaComponent = DaggerMediaGridComponent.builder()
                .applicationComponent(appComponent)
                .build();
        presenterCache = appComponent.presenterCache();
        presenter = presenterCache.getPresenter();
        if (presenter == null) {
            presenter = mediaComponent.gridPresenter();
        }
        adapter = mediaComponent.mediaAdapter();
        adapter.setOnItemClickListener(this);
        presenter.bindView(this);
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
    public void onItemClicked(int position) {
        presenter.requestDownload(position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.handlePermissionResponse(requestCode, grantResults);
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

    public void updateGrid(List<MediaItemViewModel> mediaItems) {
        adapter.update(mediaItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void searchMediaOnSubmit(Observable<SearchViewQueryTextEvent> queryTextEvents) {
        presenter.searchMediaOnSubmit(queryTextEvents);
    }

    @Override
    public void showProgressBar() {
        callback.onMediaSearchRequested();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showGrid() {
        mediaGrid.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGrid() {
        mediaGrid.setVisibility(View.GONE);
    }

    public interface Callback {
        void onMediaSearchRequested();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterCache.setPresenter(presenter);
        stateSaved = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.restoreViewState();
    }

    @Override
    public void onDestroy() {
        if (!stateSaved) {
            presenterCache.setPresenter(null);
        }
        super.onDestroy();
    }
}
