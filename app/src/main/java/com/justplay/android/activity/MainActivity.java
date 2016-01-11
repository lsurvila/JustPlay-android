package com.justplay.android.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.justplay.android.fragment.MainActivityFragment;
import com.justplay.android.R;
import com.justplay.android.network.JustPlayApi;
import com.justplay.android.network.response.SearchResponse;
import com.justplay.android.presenter.MediaGridPresenter;
import com.justplay.android.view.MediaGridView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import rx.Observable;

public class MainActivity extends RxAppCompatActivity implements MediaGridView {

    private MediaGridPresenter presenter;
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
        JustPlayApi justPlayApi = new JustPlayApi();
        presenter = new MediaGridPresenter(this, justPlayApi);
        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
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
