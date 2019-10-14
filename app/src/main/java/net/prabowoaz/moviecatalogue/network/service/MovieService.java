package net.prabowoaz.moviecatalogue.network.service;

import net.prabowoaz.moviecatalogue.Film;
import net.prabowoaz.moviecatalogue.network.Config;
import net.prabowoaz.moviecatalogue.network.MyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET(Config.API_MOVIE)
    Call<MyResponse<List<Film>>> apiMovie(
            @Query(Config.KEY) String apiKey,
            @Query(Config.LANGUAGE) String language
    );

    @GET(Config.API_TV)
    Call<MyResponse<List<Film>>> apiTv(
            @Query(Config.KEY) String apiKey,
            @Query(Config.LANGUAGE) String language
    );

    @GET(Config.API_SEARCH)
    Call<MyResponse<List<Film>>> apiSearch(
            @Query(Config.KEY) String apiKey,
            @Query(Config.LANGUAGE) String language,
            @Query(Config.QUERY) String query
    );
}
