package net.prabowoaz.moviecatalogue.network;

import net.prabowoaz.moviecatalogue.BuildConfig;

public class Config {
    public static final String API_URL = BuildConfig.BASE_URL;

    public static final String API_MOVIE = "3/discover/movie";
    public static final String API_TV = "3/discover/tv";
    public static final String API_SEARCH = "3/search/movie";

    // TODO: PARAMS
    public static final String TOKEN = "37a8a3c9242c1882a9221f94cf3ae6db";
    public static final String KEY = "api_key";
    public static final String LANGUAGE = "language";
    public static final String QUERY = "query";

    // TODO: Field Database
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_PATH = "poster_path";
    public static final String FIELD_OVERVIEW = "overview";
    public static final String FIELD_RELEASE = "release_date";
    public static final String CURRENT_DATA = "currentdata";
    public static final String MOVIE_TITLE = "movie_title";

    // TODO: NOTIFICATION CHANNEL
    public static final String CHANNEL_NAME = "notif";
    public static final int ALARM_MOVIE_ID = 1;
    public static final int ALARM_RELEASE_ID = 2;
    public static final String STATUS = "status";
}
