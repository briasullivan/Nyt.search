package com.codepath.nytsearch.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by briasullivan on 10/18/16.
 */
public interface NewYorkTimesService {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/svc/search/v2/articlesearch.json")
    public Call<NewYorkTimesResponse> listArticles(@Query("q") String query, @Query("page") int page);
}
