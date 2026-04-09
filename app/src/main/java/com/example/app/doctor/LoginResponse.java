package com.example.app.doctor;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access") public String access;
    @SerializedName("refresh") public String refresh;
}
