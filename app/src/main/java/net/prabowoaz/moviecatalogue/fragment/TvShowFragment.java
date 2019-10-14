package net.prabowoaz.moviecatalogue.fragment;


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
import android.widget.ProgressBar;
import android.widget.Toast;

import net.prabowoaz.moviecatalogue.DetailActivity;
import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.R;
import net.prabowoaz.moviecatalogue.adapters.MyAdapter;
import net.prabowoaz.moviecatalogue.listener.OnItemClickListener;
import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.network.MyResponse;
import net.prabowoaz.moviecatalogue.network.ServiceGenerator;
import net.prabowoaz.moviecatalogue.network.service.MovieService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MyAdapter adapter;
    private RecyclerView rvMovie;
    private ProgressBar progressBar;
    private List<Film> datas;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static TvShowFragment newInstance() {
        return new TvShowFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_show, container, false);
        initializeView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyAdapter(getActivity());
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datas = new ArrayList<>();

        if (savedInstanceState!=null)
            setupAdapter(savedInstanceState.<Film>getParcelableArrayList("currentdata"));
        else
            loadData();
    }

    private void setupAdapter(List<Film> data) {
        datas.addAll(data);
        adapter.addAll(data);

        initListener();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("currentdata", (ArrayList<? extends Parcelable>) datas);
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        MovieService service = ServiceGenerator.createBaseService(getContext(), MovieService.class);
        Call<MyResponse<List<Film>>> call = service.apiTv(Config.TOKEN, "en-US");
        call.enqueue(new Callback<MyResponse<List<Film>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Film>>> call, Response<MyResponse<List<Film>>> response) {
                MyResponse<List<Film>> data = response.body();

                if(!data.isSuccess()) {
                    progressBar.setVisibility(View.GONE);
                    setupAdapter(data.getResults());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<Film>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeView(View rootView) {
        rvMovie = rootView.findViewById(R.id.rvTvShow);
        progressBar = rootView.findViewById(R.id.loading);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
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
