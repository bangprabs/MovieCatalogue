package net.prabowoaz.moviecatalogue;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.schedule.AlarmNotification;
import net.prabowoaz.moviecatalogue.util.Preferences;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    Switch switchRelease, switchDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.setting));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        switchRelease.setChecked(Preferences.getBoolPref(this, Preferences.PREF_RELEASE));
        switchDaily.setChecked(Preferences.getBoolPref(this, Preferences.PREF_DAILY));

        initListener();
    }

    private void initListener() {
        final Calendar calendar = Calendar.getInstance();

        switchRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        8, 0, 0);
                if(isChecked) {
                    Preferences.saveBoolPref(SettingActivity.this, Preferences.PREF_RELEASE, true);
                    scheduleAlarm(calendar.getTimeInMillis(), "Si Doel The Movie", "Si Doel The Movie has been released", Config.ALARM_RELEASE_ID, true);
                } else {
                    stopAlarm(Config.ALARM_RELEASE_ID);
                    Preferences.clear(SettingActivity.this, Preferences.PREF_RELEASE);
                }
            }
        });

        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        6, 0, 0);
                if(isChecked) {
                    Preferences.saveBoolPref(SettingActivity.this, Preferences.PREF_DAILY, true);
                    scheduleAlarm(calendar.getTimeInMillis(), "Notifikasi", "Selamat Pagi, Cek Film Ke Sukaan Mu Hari Ini !", Config.ALARM_MOVIE_ID, false);
                } else {
                    Preferences.clear(SettingActivity.this, Preferences.PREF_DAILY);
                    stopAlarm(Config.ALARM_MOVIE_ID);
                }
            }
        });
    }

    private void initViews() {
        switchDaily = findViewById(R.id.switch_daily);
        switchRelease = findViewById(R.id.switch_release);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
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

    public void stopAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), AlarmNotification.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, id);

        alarmManager.cancel(pi);
    }
}
