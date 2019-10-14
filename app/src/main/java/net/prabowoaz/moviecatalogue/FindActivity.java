package net.prabowoaz.moviecatalogue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import net.prabowoaz.moviecatalogue.adapters.MyAdapter;
import net.prabowoaz.moviecatalogue.listener.OnItemClickListener;
import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.network.MyResponse;
import net.prabowoaz.moviecatalogue.network.ServiceGenerator;
import net.prabowoaz.moviecatalogue.network.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindActivity extends AppCompatActivity {

    RecyclerView rv_search;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeView();

        String movie_title = getIntent().getStringExtra(Config.MOVIE_TITLE);
        loadData(movie_title);

        setupList();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pencarian Untuk : " + movie_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeView() {
        rv_search = findViewById(R.id.rv_search);
    }

    private void setupList() {
        adapter = new MyAdapter(this);
        rv_search.setLayoutManager(new LinearLayoutManager(this));
        rv_search.setAdapter(adapter);

        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(FindActivity.this, DetailActivity.class);
                i.putExtra(DetailActivity.EXTRA_DETAIL, adapter.getData(position));
                startActivity(i);
            }
        });
    }

    private void loadData(String movie_title) {
        MovieService service = ServiceGenerator.createBaseService(getApplicationContext(), MovieService.class);
        Call<MyResponse<List<Film>>> call = service.apiSearch(Config.TOKEN, "en-US", movie_title);
        call.enqueue(new Callback<MyResponse<List<Film>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Film>>> call, Response<MyResponse<List<Film>>> response) {
                if (response.isSuccessful()) {
                    adapter.addAll(response.body().getResults());
                } else {
                    loadFailed();
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<Film>>> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(this, R.string.err_load_failed, Toast.LENGTH_SHORT).show();
    }
}
