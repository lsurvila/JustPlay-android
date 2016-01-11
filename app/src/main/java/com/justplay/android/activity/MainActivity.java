package com.justplay.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private JustPlayApi justPlayApi;
    private MainActivityFragment fragment;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        justPlayApi = new JustPlayApi();
        fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                RxSearchView.queryTextChangeEvents(searchView)
                        .flatMap(event -> {
                            if (event.isSubmitted()) {
                                return justPlayApi.search(event.queryText().toString())
                                        .subscribeOn(Schedulers.io());
                            }
                            return Observable.empty();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchResponses -> {
                            if (fragment != null) {
                                fragment.updateGrid(searchResponses);
                            }
                        }, error -> {
                            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
