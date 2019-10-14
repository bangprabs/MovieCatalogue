package net.prabowoaz.myapplication;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import net.prabowoaz.myapplication.adapters.MyAdapter;
import net.prabowoaz.myapplication.listeners.ProviderLoadCallback;

import java.lang.ref.WeakReference;
import java.util.List;

import static net.prabowoaz.myapplication.database.DatabaseContract.FilmColumns.CONTENT_URI;
import static net.prabowoaz.myapplication.helper.MappingHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements ProviderLoadCallback {
    RecyclerView recyclerData;

    private DataObserver myObserve;
    private MyAdapter adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        adapters = new MyAdapter(this);
        recyclerData.setLayoutManager(new LinearLayoutManager(this));
        recyclerData.setAdapter(adapters);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserve = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserve);
        new getData(this, this).execute();
    }

    private void initViews() {
        recyclerData = findViewById(R.id.rv_myapplication);
    }

    @Override
    public void postExecute(Cursor movies) {
        List<Film> dataList = mapCursorToArrayList(movies);
        if (dataList.size() > 0) {
            adapters.addAll(dataList);
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<ProviderLoadCallback> weakCallback;


        private getData(Context context, ProviderLoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }
}
