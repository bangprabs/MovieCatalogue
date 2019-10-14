package net.prabowoaz.myapplication.helper;

import android.database.Cursor;

import net.prabowoaz.myapplication.Film;
import net.prabowoaz.myapplication.database.DatabaseContract;

import java.util.ArrayList;
import java.util.List;

import static net.prabowoaz.myapplication.database.DatabaseContract.getColumnInt;
import static net.prabowoaz.myapplication.database.DatabaseContract.getColumnString;

public class MappingHelper {

    public static List<Film> mapCursorToArrayList(Cursor cursor) {
        List<Film> dataList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = getColumnInt(cursor, DatabaseContract.FilmColumns.KEY_ID);
            String title = getColumnString(cursor, DatabaseContract.FilmColumns.KEY_TITLE);
            String poster_path = getColumnString(cursor, DatabaseContract.FilmColumns.KEY_PATH);
            String overview = getColumnString(cursor, DatabaseContract.FilmColumns.KEY_OVERVIEW);
            String release_date = getColumnString(cursor, DatabaseContract.FilmColumns.KEY_RELEASE);
            dataList.add(new Film(id, title, poster_path, overview, release_date));
        }

        return dataList;
    }
}