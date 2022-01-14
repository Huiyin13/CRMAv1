package com.example.crmav1.ManageChat;

import com.example.crmav1.Notifications.MyResponse;
import com.example.crmav1.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAh6DSu4:APA91bGAy7_B5sQ9cAKCAZwiGLc_BLXDPpzq5B3E3qcdWLueAx9ZZEgdauirakdGqMZ1yT5c7t8PGEVxp_Ue20fXk7eplGV-NxsFm-OYylscVaiLzIQRSQlcVK8uut6QgujnNYNm9P09"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
