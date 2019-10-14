package net.prabowoaz.moviecatalogue.provider.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmHelper {
    private static final String TABLE_NAME = "movies";
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public FilmHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public List<Film> getFilms() {
        List<Film> array_list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME, null);

        while (res.moveToNext()) {
            Film data = new Film();
            data.setId(res.getInt(0));
            data.setTitle(res.getString(1));
            data.setPoster_path(res.getString(2));
            data.setOverview(res.getString(3));
            data.setRelease_date(res.getString(4));
            array_list.add(data);
        }
        res.close();

        return array_list;
    }

    public Film getFilm(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME + "  where id=" + id + "", null);
        if (!res.moveToFirst()) {
            res.close();
            return null;
        }

        Film data = new Film();
        data.setId(res.getInt(0));
        data.setTitle(res.getString(1));
        data.setPoster_path(res.getString(2));
        data.setOverview(res.getString(3));
        data.setRelease_date(res.getString(4));

        res.close();
        return data;
    }

    public Cursor queryProvider() {
        return database.query(TABLE_NAME
                , null
                , null
                , null
                , null
                , null
                , null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_NAME, null
                , "id = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }
}
