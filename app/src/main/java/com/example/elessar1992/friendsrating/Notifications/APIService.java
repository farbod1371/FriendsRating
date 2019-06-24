package com.example.elessar1992.friendsrating.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by elessar1992 on 6/21/19.
 */

public interface APIService
{
    @Headers({
            "Context-Type:application/json",
            "Authorization:key=AAAAlVCcXr8:APA91bHmDo00JCIo99dV7yYABrgWDldl92Uylm31e0BNFPhUeB3CEk0NTZxcafoXyn42UwkAyjPoNrdguJreUXLz433zQRBchppUoDnmAHXX4l5Wtk2BrzOnF_hofp93f4hXpuIjhXMk"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
