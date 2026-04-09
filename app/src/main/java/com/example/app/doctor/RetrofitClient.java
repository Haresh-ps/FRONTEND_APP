package com.example.app.doctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://csp40zdq-8000.inc1.devtunnels.ms/";
    
    private static Retrofit retrofit = null;
    private static String authToken = null;

    public static void setAuthToken(String token) {
        if (token != null && !token.equals(authToken)) {
            authToken = token;
            retrofit = null; // Reset to ensure new client with new token is built
        }
    }

    public static ApiService getApiService() {
        return getApiService(null);
    }

    public static ApiService getApiService(Context context) {
        // Load token from SharedPreferences if missing in memory
        if (authToken == null && context != null) {
            SharedPreferences prefs = context.getSharedPreferences("DoctorProfile", Context.MODE_PRIVATE);
            authToken = prefs.getString("auth_token", null);
            if (authToken != null) {
                retrofit = null; // Force rebuild with the loaded token
            }
        }

        if (retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            httpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                
                requestBuilder.header("X-Tunnel-Skip-Anti-Phishing-Page", "true");

                if (authToken != null) {
                    requestBuilder.header("Authorization", "Bearer " + authToken);
                }
                return chain.proceed(requestBuilder.build());
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
