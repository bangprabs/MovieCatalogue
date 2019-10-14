package net.prabowoaz.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.R;
import net.prabowoaz.moviecatalogue.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Film> itemWidgets;
    DatabaseHelper dbHelpers;
    Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
        itemWidgets = new ArrayList<>();
        dbHelpers = new DatabaseHelper(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (dbHelpers.getFilms(false) != null && dbHelpers.getFilms(false).size() > 0)
            itemWidgets.addAll(dbHelpers.getFilms(false));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return itemWidgets.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tv_desc, itemWidgets.get(position).getName());
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w500" + itemWidgets.get(position).getPoster_path())
                    .submit(412, 412)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
