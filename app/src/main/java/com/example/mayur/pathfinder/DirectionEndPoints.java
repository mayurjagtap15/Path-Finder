package com.example.mayur.pathfinder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DirectionEndPoints {

    @GET()

    public Call<DirectionResponse> getDirectionData(@Url String url);

}
