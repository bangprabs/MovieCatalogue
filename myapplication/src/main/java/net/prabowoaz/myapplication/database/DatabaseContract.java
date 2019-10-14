package net.prabowoaz.myapplication.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
    public static final String AUTHORITY = "net.prabowoaz.moviecatalogue";
    private static final String SCHEME = "content";

    public DatabaseContract() {
    }

    public static final class FilmColumns implements BaseColumns {
        public final static String DB_NAME = "favorite";
        public final static String TABLE_NAME = "movies";

        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_PATH = "poster_path";
        public static final String KEY_OVERVIEW = "overview";
        public static final String KEY_RELEASE = "release_date";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
