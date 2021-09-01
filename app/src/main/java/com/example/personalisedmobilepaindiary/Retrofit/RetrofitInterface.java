package com.example.personalisedmobilepaindiary.Retrofit;

import com.example.personalisedmobilepaindiary.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    String BASE_PARAM = "weather?q=Melbourne,Au&appid=34a3e954490b05352646218f715c7da3&units=metric";

    @GET(BASE_PARAM)
    Call<WeatherResponse> getWeather();
}

/*
weather?q=Melbourne,Au&appid=34a3e954490b05352646218f715c7da3/*/
