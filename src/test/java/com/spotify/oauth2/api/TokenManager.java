package com.spotify.oauth2.api;

import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;


public class TokenManager {
    private static String access_token;
    private static Instant expiry_date;

    public static String getToken() {
        try {
            if (access_token == null || Instant.now().isAfter(expiry_date)) {
                System.out.println("Renewing token...");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_date = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            } else {
                System.out.println("Token is good to use");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get token");
        }
        return "Bearer " + access_token;
    }

    private static Response renewToken() {
        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "refresh_token");
        formParams.put("refresh_token", "AQCOUnH6T0rnQO8Cmb0aEdoAG67M7dl3SwH-YpKpt_238eTB2l-3X8zffIqkezbnqtzw2jW3m0wTXOcIHcIiiVz8tF1BTak91tUPw-YcbTZyvFdDxzCwKNahlJdY7I3Pbss");
        formParams.put("client_id", "6489b5c605664bdeaf0a43247b307993");
        formParams.put("client_secret", "b33426a25175435eb3283afd59cf7601");

        Response response = RestResource.postAccount(formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Renew token failed!");
        }
        return response;
    }
}
