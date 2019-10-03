package com.merttoptas.bringit.Activity.Fragment;
import com.merttoptas.bringit.Activity.Notifications.MyResponse;
import com.merttoptas.bringit.Activity.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAm-KPWyU:APA91bFArok5hrQ2H999z8orJBTUqbrb6C0VNaUOSW4QWVXjAsuA818xkeuatXGEEo4xZPZ-OJMpQVJyvFlmd-N7xeu-9yIpdM-seoBsn9AJB8D-fA7FTxg44isYBbdeQTciDnL2i84j"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
