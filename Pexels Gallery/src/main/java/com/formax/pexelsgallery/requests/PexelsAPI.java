package com.formax.pexelsgallery.requests;

import com.formax.pexelsgallery.models.PexelsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PexelsAPI {


    @GET("curated")
    Call<PexelsResponse> getCuratedPhotos(@Query("per_page") int perPage,
                                          @Query("page") int page);

    @GET("search")
    Call<PexelsResponse> searchPhotos(@Query("query") String query,
                                      @Query("per_page") int perPage,
                                      @Query("page") int page);

    @GET("curated")
    Call<PexelsResponse> getRandomPhoto(@Query("per_page") int perPage,
                                        @Query("page") int page);

}
