package com.justplay.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends Fragment {

    @Bind(R.id.media_grid)
    RecyclerView mediaGrid;

    private MediaItemAdapter adapter;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        JustPlayApi api = new JustPlayApi();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mediaGrid.setLayoutManager(layoutManager);
        adapter = new MediaItemAdapter();
        adapter.setOnItemClickListener(position -> {
            SearchResponse item = adapter.getItem(position);
            Toast.makeText(getContext(), "Downloading " + item.getTitle(), Toast.LENGTH_SHORT).show();
            api.download(item.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        Toast.makeText(getContext(), "Downloaded to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }, error -> {
                        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        mediaGrid.setAdapter(adapter);
        return view;
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
