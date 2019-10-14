package net.prabowoaz.moviecatalogue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.network.Config;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "favorite";
    private final static String TABLE_MOVIES = "movies";
    private final static String TABLE_TV_SHOW = "tvshow";
    private final static int VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table  " + TABLE_MOVIES +
                        " (id integer primary key, title text, poster_path text, overview text, release_date text)"
        );

        db.execSQL(
                "create table  " + TABLE_TV_SHOW +
                        " (id integer primary key, title text, poster_path text, overview text, release_date text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        db.execSQL("DROP TABLE IF EXISTS tvshow");
        onCreate(db);
    }

    public void insertFilm(Film data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.FIELD_ID, data.getId());
        contentValues.put(Config.FIELD_TITLE, data.getTitle() != null ? data.getTitle() : data.getName());
        contentValues.put(Config.FIELD_PATH, data.getPoster_path());
        contentValues.put(Config.FIELD_OVERVIEW, data.getOverview());
        contentValues.put(Config.FIELD_RELEASE, data.getRelease_date() != null ? data.getRelease_date() : data.getFirst_air_date());

        db.insert(data.getTitle() != null ? TABLE_MOVIES : TABLE_TV_SHOW, null, contentValues);
    }

    public List<Film> getFilms(boolean isMovie) {
        List<Film> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String TABLE_NAME = isMovie ? TABLE_MOVIES : TABLE_TV_SHOW;

        Cursor res = null;
        if (isMovie)
            res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME, null);
        else
            res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME, null);

        while (res.moveToNext()) {
            Film data = new Film();

            data.setId(res.getInt(0));
            if (isMovie) {
                data.setTitle(res.getString(1));
                data.setRelease_date(res.getString(4));
            } else
                data.setName(res.getString(1));
                data.setFirst_air_date(res.getString(4));


            data.setPoster_path(res.getString(2));
            data.setOverview(res.getString(3));
            dataList.add(data);
        }

        res.close();
        return dataList;
    }

    public Film getFilm(int id, boolean isMovie) {
        SQLiteDatabase db = this.getReadableDatabase();
        String TABLE_NAME = isMovie ? TABLE_MOVIES : TABLE_TV_SHOW;

        Cursor res = null;
        if (isMovie)
            res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME + "  where id=" + id + "", null);
        else
            res = db.rawQuery("select id, title, poster_path, overview, release_date from " + TABLE_NAME + "  where id=" + id + "", null);

        if (!res.moveToFirst()) {
            res.close();
            return null;
        }

        Film data = new Film();
        data.setId(res.getInt(0));

        if (isMovie) {
            data.setTitle(res.getString(1));
            data.setRelease_date(res.getString(4));
        } else {
            data.setName(res.getString(1));
            data.setFirst_air_date(res.getString(4));


            data.setPoster_path(res.getString(2));
            data.setOverview(res.getString(3));
        }

        res.close();
        return data;
    }

    public void deleteFilm(int id, boolean isMovie) {
        SQLiteDatabase db = this.getWritableDatabase();
        String TABLE_NAME = isMovie ? TABLE_MOVIES : TABLE_TV_SHOW;

        db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }
}
