package com.justplay.android.view.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.justplay.android.DaggerMediaSearchComponent;
import com.justplay.android.MediaSearchModule;
import com.justplay.android.MediaSearchComponent;
import com.justplay.android.R;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.presenter.MediaSearchPresenter;
import com.justplay.android.view.MediaSearchView;
import com.justplay.android.view.fragment.MainActivityFragment;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class MainActivity extends RxAppCompatActivity implements MediaSearchView {

    @Inject MediaSearchPresenter presenter;
    private MainActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        injectDependencies();
    }

    private void injectDependencies() {
        presenter = new MediaSearchPresenter();
        MediaSearchComponent component = DaggerMediaSearchComponent.builder()
                .mediaSearchModule(new MediaSearchModule(this))
                .build();
        component.injectPresenter(presenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                presenter.searchMediaOnSubmit(RxSearchView.queryTextChangeEvents(searchView));
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void updateGrid(List<SearchResponse> searchResponses) {
        if (fragment != null) {
            fragment.updateGrid(searchResponses);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Observable<ActivityEvent> getLifecycle() {
        return lifecycle();
    }

}
