package net.prabowoaz.moviecatalogue;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import net.prabowoaz.moviecatalogue.adapters.ViewPagerAdapter;
import net.prabowoaz.moviecatalogue.fragment.MovieFragment;
import net.prabowoaz.moviecatalogue.fragment.TvShowFragment;
import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.schedule.AlarmNotification;
import net.prabowoaz.moviecatalogue.util.Preferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        setupAlarm();

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.appName);
            getSupportActionBar().setElevation(0);
        }

        List<String> title = new ArrayList<>();
        List<Fragment> fragment = new ArrayList<>();

        title.add(getResources().getString(R.string.movie));
        title.add(getResources().getString(R.string.tv_show));
        fragment.add(MovieFragment.newInstance());
        fragment.add(TvShowFragment.newInstance());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, fragment, title);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void initializeView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setQueryHint(getResources().getString(R.string.label_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                intent.putExtra(Config.MOVIE_TITLE, query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favorite:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;
            case R.id.language_en:
                changeLanguage("en");
                return true;
            case R.id.language_in:
                changeLanguage("id");
                return true;
            case R.id.settings:
                Intent isetting = new Intent(this, SettingActivity.class);
                startActivity(isetting);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeLanguage(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private void setupAlarm() {
        if(!Preferences.getBoolPref(this, Preferences.PREE_OPEN_APPS)) {
            Preferences.saveBoolPref(this, Preferences.PREE_OPEN_APPS, true);
            Preferences.saveBoolPref(this, Preferences.PREF_DAILY, true);
            Preferences.saveBoolPref(this, Preferences.PREF_RELEASE, true);

            Calendar calendar = Calendar.getInstance();

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    8 , 0, 0);
            scheduleAlarm(calendar.getTimeInMillis(), "Si Doel The Movie", "Si Doel The Movie has been released", Config.ALARM_RELEASE_ID, true);

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    7, 0, 0);
            scheduleAlarm(calendar.getTimeInMillis(), "Notifikasi", "Selamat Pagi, Cek Film Ke Sukaan Mu Yuk !", Config.ALARM_MOVIE_ID, false);
        }
    }

    public void scheduleAlarm(long time, String title, String body, int id, boolean status) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmNotification.class);
        i.putExtra("title", title);
        i.putExtra("body", body);
        i.putExtra("status", status);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, id);

        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
    }
}
