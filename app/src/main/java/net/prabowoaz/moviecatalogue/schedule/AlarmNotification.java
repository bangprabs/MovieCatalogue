package net.prabowoaz.moviecatalogue.schedule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.MainActivity;
import net.prabowoaz.moviecatalogue.R;
import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.network.MyResponse;
import net.prabowoaz.moviecatalogue.network.ServiceGenerator;
import net.prabowoaz.moviecatalogue.network.service.MovieService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmNotification extends BroadcastReceiver {
    private static final String TAG = AlarmNotification.class.getSimpleName();
    final int min = 10;
    final int max = 100;
    final int random = new Random().nextInt((max - min) + 1) + min;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String title = intent.getStringExtra(Config.FIELD_TITLE), body = intent.getStringExtra("body");
        boolean isReleased = intent.getBooleanExtra(Config.STATUS, false);

        if (isReleased) {
            loadMovieNotif();
            loadTvNotif();
        } else {
            createNotif(context, title, body);
        }
    }

    private void loadMovieNotif() {
        MovieService service = ServiceGenerator.createBaseService(context, MovieService.class);
        Call<MyResponse<List<Film>>> call = service.apiMovie(Config.TOKEN, "en-US");
        call.enqueue(new Callback<MyResponse<List<Film>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Film>>> call, Response<MyResponse<List<Film>>> response) {
                MyResponse<List<Film>> datas = response.body();

                if (!datas.isSuccess()) {
                    for (Film data : datas.getResults()) {
                        if (changeDate(new Date()).equals(data.getRelease_date()))
                            createNotif(context, data.getTitle(), data.getOverview().length() < 100 ? data.getOverview() : data.getOverview().subSequence(0, 100) + " ...");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<Film>>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void loadTvNotif() {
        MovieService service = ServiceGenerator.createBaseService(context, MovieService.class);
        Call<MyResponse<List<Film>>> call = service.apiTv(Config.TOKEN, "en-US");
        call.enqueue(new Callback<MyResponse<List<Film>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<Film>>> call, Response<MyResponse<List<Film>>> response) {
                MyResponse<List<Film>> datas = response.body();

                if (!datas.isSuccess()) {
                    for (Film data : datas.getResults()) {
                        if (changeDate(new Date()).equals(data.getFirst_air_date()))
                            createNotif(context, data.getTitle(), data.getOverview().length() < 100 ? data.getOverview() : data.getOverview().subSequence(0, 100) + " ...");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<Film>>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public String changeDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateFormat = format.format(date);
        return dateFormat;
    }

    private void createNotif(Context context, String title, String body) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, random, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Config.CHANNEL_NAME, Config.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("Notification");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(context, Config.CHANNEL_NAME)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.TRANSPARENT)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[]{0L})
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify("tag", random, notificationBuilder.build());
    }
}
