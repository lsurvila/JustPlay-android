package com.justplay.android.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.justplay.android.R;
import com.justplay.android.model.SearchViewModel;
import com.justplay.android.view.fragment.MainActivityFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String KEY_SEARCH_VIEW_STATE = "key_search_view_state";

    private MainActivityFragment fragment;
    private SearchViewModel searchViewModel;
    private SearchView searchView;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                if (searchViewModel != null) {
                    restoreSearchViewState();
                }
                if (fragment != null) {
                    fragment.searchMediaOnSubmit(RxSearchView.queryTextChangeEvents(searchView));
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void restoreSearchViewState() {
        searchView.setQuery(searchViewModel.getQueryString(), false);
        searchView.setIconified(!searchViewModel.isExpanded());
        if (!searchViewModel.isFocused()) {
            searchView.clearFocus();
        }
    }

    @Override
    public void onMediaSearchRequested() {
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        searchViewModel = new SearchViewModel();
        if (searchView != null) {
            searchViewModel.setQueryString(searchView.getQuery().toString());
            searchViewModel.setExpanded(!searchView.isIconified());
            searchViewModel.setFocused(searchView.hasFocus());
            outState.putParcelable(KEY_SEARCH_VIEW_STATE, searchViewModel);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchViewModel = savedInstanceState.getParcelable(KEY_SEARCH_VIEW_STATE);
    }

}
