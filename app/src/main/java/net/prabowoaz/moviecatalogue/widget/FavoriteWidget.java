package net.prabowoaz.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import net.prabowoaz.moviecatalogue.DetailActivity;
import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.R;
import net.prabowoaz.moviecatalogue.database.DatabaseHelper;
import net.prabowoaz.moviecatalogue.widget.service.StackWidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {
    private List<Film> itemWidgets;
    DatabaseHelper dbHelpers;
    private static final String TOAST_ACTION = "net.prabowoaz.moviecatalogue.TOAST_ACTION";
    public static final String EXTRA_ITEM = "net.prabowoaz.moviecatalogue.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(FavoriteWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        itemWidgets = new ArrayList<>();
        dbHelpers = new DatabaseHelper(context);
        if (dbHelpers.getFilms(false) != null && dbHelpers.getFilms(false).size() > 0)
            itemWidgets.addAll(dbHelpers.getFilms(false));

        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                if(dbHelpers.getFilms(false) != null && dbHelpers.getFilms(false).size() > 0) {
                    Intent intent1 = new Intent(context, DetailActivity.class);
                    intent1.putExtra(DetailActivity.EXTRA_DETAIL, itemWidgets.get(viewIndex));
                    context.startActivity(intent1);
                }
            }
        }
    }
}