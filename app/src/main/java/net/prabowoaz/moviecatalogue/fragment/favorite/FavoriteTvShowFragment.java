package net.prabowoaz.moviecatalogue.fragment.favorite;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.prabowoaz.moviecatalogue.DetailActivity;
import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.R;
import net.prabowoaz.moviecatalogue.adapters.MyAdapter;
import net.prabowoaz.moviecatalogue.database.DatabaseHelper;
import net.prabowoaz.moviecatalogue.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTvShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MyAdapter adapter;
    private RecyclerView rvFavoriteTvShow;
    private TextView tvTanpaData;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Film> datas;
    private DatabaseHelper databaseHelper;

    public static FavoriteTvShowFragment newInstance() {
        return new FavoriteTvShowFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
        initializeView(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyAdapter(getActivity());
        rvFavoriteTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavoriteTvShow.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        datas = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());

        if (savedInstanceState!=null)
            setupAdapter(savedInstanceState.<Film>getParcelableArrayList("currentdata"));
        else
            loadData();
    }

    private void loadData() {
        if (databaseHelper.getFilms(false).size() > 0)
            setupAdapter(databaseHelper.getFilms(false));
        else
            tvTanpaData.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        loadData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("currentdata", (ArrayList<? extends Parcelable>) datas);
    }

    private void setupAdapter(List<Film> data) {
        datas.addAll(data);
        adapter.addAll(data);

        initListener();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_DETAIL, adapter.getData(position));
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initializeView(View rootView) {
        rvFavoriteTvShow = rootView.findViewById(R.id.rvFavoriteTv);
        tvTanpaData = rootView.findViewById(R.id.tv_nodata);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                adapter.clear();
                loadData();
            }
        }, 2000);
    }
}
