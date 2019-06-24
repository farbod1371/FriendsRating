package com.example.elessar1992.friendsrating.Notifications;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by elessar1992 on 6/20/19.
 */

public class Client
{
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(String url)
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
