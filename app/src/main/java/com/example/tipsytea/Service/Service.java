package com.example.tipsytea.Service;

import com.example.tipsytea.Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service {
    @POST("/services/oauth2/token?grant_type=password&client_id=3MVG9fe4g9fhX0E6waLmzNEpJuWI0Muer0.nzXd4G0qOaOyg7UXBhbucv9B6ZQpjNnX5LaVktdYUUTAouPnMH&client_secret=472ED5DBE6CA9C1D15CB11140B5B5456DBA33E58052C0C06C2A855689C9BC1EC")
    Call login(@Body Login login);

    @GET("/services/oauth2/token?grant_type=password&client_id=3MVG9fe4g9fhX0E6waLmzNEpJuWI0Muer0.nzXd4G0qOaOyg7UXBhbucv9B6ZQpjNnX5LaVktdYUUTAouPnMH&client_secret=472ED5DBE6CA9C1D15CB11140B5B5456DBA33E58052C0C06C2A855689C9BC1EC")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
}
