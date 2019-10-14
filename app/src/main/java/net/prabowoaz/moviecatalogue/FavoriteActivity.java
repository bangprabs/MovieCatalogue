package net.prabowoaz.moviecatalogue;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import net.prabowoaz.moviecatalogue.adapters.ViewPagerAdapter;
import net.prabowoaz.moviecatalogue.fragment.favorite.FavoriteMovieFragement;
import net.prabowoaz.moviecatalogue.fragment.favorite.FavoriteTvShowFragment;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initalizeView();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.favorite));
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        List<String> title = new ArrayList<>();
        List<Fragment> fragment = new ArrayList<>();

        title.add(getResources().getString(R.string.movie));
        title.add(getResources().getString(R.string.tv_show));

        fragment.add(FavoriteMovieFragement.newInstance());
        fragment.add(FavoriteTvShowFragment.newInstance());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, fragment, title);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void initalizeView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
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
}
