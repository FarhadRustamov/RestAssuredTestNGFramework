package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
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
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = RestResource.postAccount(formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Renew token failed!");
        }
        return response;
    }
}
