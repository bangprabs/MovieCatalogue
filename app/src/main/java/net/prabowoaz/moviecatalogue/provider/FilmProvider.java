package net.prabowoaz.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.prabowoaz.moviecatalogue.provider.database.FilmHelper;

import java.sql.SQLException;

public class FilmProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    public static final String AUTHORITY = "net.prabowoaz.moviecatalogue";
    private static final String TABLE_NAME = "movies";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FilmHelper filmHelper;
    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        filmHelper = new FilmHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            filmHelper.open();

            Cursor cursor;
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    cursor = filmHelper.queryProvider();
                    break;
                case MOVIE_ID:
                    cursor = filmHelper.queryByIdProvider(uri.getLastPathSegment());
                    break;
                default:
                    cursor = null;
                    break;
            }

            return cursor;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
